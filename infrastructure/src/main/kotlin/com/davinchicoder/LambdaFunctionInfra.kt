package com.davinchicoder

import software.amazon.awscdk.Duration
import software.amazon.awscdk.Stack
import software.amazon.awscdk.services.iam.IRole
import software.amazon.awscdk.services.lambda.Architecture
import software.amazon.awscdk.services.lambda.Code
import software.amazon.awscdk.services.lambda.Function
import software.amazon.awscdk.services.lambda.Runtime
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource
import software.amazon.awscdk.services.sqs.IQueue

class LambdaFunctionInfra(val stack: Stack, val lambdaRole: IRole, val sqsQueue: IQueue) {

    lateinit var lambdaFunction: Function

    init {
        initializeResources()
    }

    fun initializeResources() {

        lambdaFunction = Function.Builder.create(stack, "lambda-consumer")
            .functionName("lambda-consumer")
            .role(lambdaRole)
            .runtime(Runtime.JAVA_21)
            .architecture(Architecture.X86_64)
            .handler("com.davinchicoder.Handler::handleRequest")
            .code(Code.fromAsset("../build/libs/lambda.zip"))
            .events(listOf(SqsEventSource.Builder.create(sqsQueue).batchSize(10).build()))
            .memorySize(1024)
            .timeout(Duration.seconds(10))
            .retryAttempts(2)
            .environment(mapOf("ENV" to stack.environment))
            .build()
    }

}