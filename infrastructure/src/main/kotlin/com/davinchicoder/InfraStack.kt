package com.davinchicoder

import software.amazon.awscdk.Duration
import software.amazon.awscdk.RemovalPolicy
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.iam.ManagedPolicy
import software.amazon.awscdk.services.iam.Role
import software.amazon.awscdk.services.iam.ServicePrincipal
import software.amazon.awscdk.services.kms.Key
import software.amazon.awscdk.services.lambda.Architecture
import software.amazon.awscdk.services.lambda.Code
import software.amazon.awscdk.services.lambda.Function
import software.amazon.awscdk.services.lambda.Runtime
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource
import software.amazon.awscdk.services.sqs.DeadLetterQueue
import software.amazon.awscdk.services.sqs.Queue
import software.constructs.Construct

class InfraStack(
    scope: Construct,
    id: String,
    props: StackProps? = null
) : Stack(scope, id, props) {

    fun initializeResources() {
        
        val kmsKey = Key.Builder.create(this, "KmsKey")
            .alias("KmsKeyAlias")
            .enableKeyRotation(false)
            .pendingWindow(Duration.days(7))
            .removalPolicy(RemovalPolicy.DESTROY)
            .description("KmsKeyDescription")
            .build()

        val sqsDlq = Queue.Builder.create(this, "SqsDlq")
            .queueName("SqsDlq")
            .deliveryDelay(Duration.seconds(10))
            .encryptionMasterKey(kmsKey)
            .fifo(false)
            .build()

        val deadLetterQueue = DeadLetterQueue.builder()
            .maxReceiveCount(5)
            .queue(sqsDlq)
            .build()

        val sqsQueue = Queue.Builder.create(this, "SqsQueue")
            .queueName("SqsQueue")
            .deliveryDelay(Duration.seconds(10))
            .encryptionMasterKey(kmsKey)
            .deadLetterQueue(deadLetterQueue)
            .fifo(false)
            .build()

        val lambdaRole = Role.Builder.create(this, "LambdaRole")
            .assumedBy(ServicePrincipal("lambda.amazonaws.com"))
            .managedPolicies(
                listOf(
                    ManagedPolicy.fromAwsManagedPolicyName("service-role/AWSLambdaBasicExecutionRole")
                )
            )
            .build()

        sqsQueue.grantConsumeMessages(lambdaRole)

        Function.Builder.create(this, "Lambda")
            .functionName("LambdaFunction")
            .role(lambdaRole)
            .runtime(Runtime.JAVA_21)
            .architecture(Architecture.X86_64)
            .handler("com.davinchicoder.Handler::handleRequest")
            .code(Code.fromAsset("../build/libs/lambda.zip"))
            .events(listOf(SqsEventSource.Builder.create(sqsQueue).batchSize(10).build()))
            .memorySize(1024)
            .timeout(Duration.seconds(10))
            .retryAttempts(2)
            .environment(mapOf("ENV" to "DEV"))
            .build()
    }
}