package com.davinchicoder

import org.junit.Test
import software.amazon.awscdk.Stack
import software.amazon.awscdk.assertions.Match
import software.amazon.awscdk.assertions.Template
import software.amazon.awscdk.services.iam.Role
import software.amazon.awscdk.services.iam.ServicePrincipal
import software.amazon.awscdk.services.kms.Key
import kotlin.test.assertNotNull

class SqsInfraTest {

    @Test
    fun `test initializeResources creates valid SQS queue and DLQ`() {

        val stack = Stack()
        val kmsKey = Key.Builder.create(stack, "TestKmsKey").build()
        val lambdaRole = Role.Builder.create(stack, "TestLambdaRole")
            .assumedBy(ServicePrincipal("lambda.amazonaws.com"))
            .build()


        val sqsInfra = SqsInfra(stack, kmsKey, lambdaRole)


        assertNotNull(sqsInfra.sqsQueue, "SQS queue should be initialized within SqsInfra")

        val template = Template.fromStack(stack)

        template.resourceCountIs("AWS::SQS::Queue", 2)

        template.hasResourceProperties(
            "AWS::SQS::Queue", mapOf(
                "QueueName" to "sqs-queue",
                "DelaySeconds" to 10,
                "KmsMasterKeyId" to Match.anyValue(),
                "RedrivePolicy" to mapOf(
                    "maxReceiveCount" to 5,
                    "deadLetterTargetArn" to Match.anyValue()
                )
            )
        )

        template.hasResourceProperties(
            "AWS::SQS::Queue", mapOf(
                "QueueName" to "sqs-dlq",
                "DelaySeconds" to 10,
                "KmsMasterKeyId" to Match.anyValue()
            )
        )
    }
}