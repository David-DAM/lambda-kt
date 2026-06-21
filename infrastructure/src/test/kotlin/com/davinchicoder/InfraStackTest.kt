package com.davinchicoder

import org.junit.Test
import software.amazon.awscdk.App
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.assertions.Match
import software.amazon.awscdk.assertions.Template
import kotlin.test.assertNotNull


class InfraStackTest {

    @Test
    fun `test InfraStack initializes resources correctly`() {
        // Arrange
        val app = App()

        // Act
        val stack = InfraStack(app, "TestInfraStack", StackProps.builder().build())
        val template = Template.fromStack(stack)

        // Assert
        assertNotNull(stack, "InfraStack should be successfully created and initialized")

        template.resourceCountIs("AWS::KMS::Key", 1)
        template.resourceCountIs("AWS::IAM::Role", 1)
        template.resourceCountIs("AWS::SQS::Queue", 2) // SQS Queue and DLQ
        template.resourceCountIs("AWS::Lambda::Function", 1)
    }

    @Test
    fun `test KMS Key has correct properties`() {
        // Arrange
        val app = App()
        val stack = InfraStack(app, "TestInfraStack", StackProps.builder().build())
        val template = Template.fromStack(stack)

        // Act & Assert
        template.hasResourceProperties(
            "AWS::KMS::Key", mapOf(
                "Description" to "key-description",
                "EnableKeyRotation" to false
            )
        )
    }

    @Test
    fun `test SQS Queue and DLQ are configured correctly`() {
        // Arrange
        val app = App()
        val stack = InfraStack(app, "TestInfraStack", StackProps.builder().build())
        val template = Template.fromStack(stack)

        // Act & Assert
        template.hasResourceProperties(
            "AWS::SQS::Queue", mapOf(
                "QueueName" to "sqs-queue",
                "DelaySeconds" to 10,
                "RedrivePolicy" to mapOf(
                    "maxReceiveCount" to 5,
                    "deadLetterTargetArn" to Match.anyValue()
                )
            )
        )

        template.hasResourceProperties(
            "AWS::SQS::Queue", mapOf(
                "QueueName" to "sqs-dlq",
                "DelaySeconds" to 10
            )
        )
    }

    @Test
    fun `test Lambda Function is configured with proper IAM Role and SQS Queue`() {
        // Arrange
        val app = App()
        val stack = InfraStack(app, "TestInfraStack", StackProps.builder().build())
        val template = Template.fromStack(stack)

        // Act & Assert
        template.hasResourceProperties(
            "AWS::Lambda::Function", mapOf(
                "Handler" to Match.anyValue(),
                "Runtime" to "java21",
                "Role" to Match.anyValue(),
                "Environment" to Match.anyValue()
            )
        )
    }
}