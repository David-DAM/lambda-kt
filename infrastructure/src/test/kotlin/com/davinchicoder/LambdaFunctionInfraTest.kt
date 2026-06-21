package com.davinchicoder

import org.junit.Test
import software.amazon.awscdk.Stack
import software.amazon.awscdk.assertions.Match
import software.amazon.awscdk.assertions.Template
import software.amazon.awscdk.services.iam.Role
import software.amazon.awscdk.services.iam.ServicePrincipal
import software.amazon.awscdk.services.sqs.Queue

class LambdaFunctionInfraTest {

    @Test
    fun `test Lambda function creation`() {
        // Arrange
        val stack = Stack()
        val role = Role.Builder.create(stack, "TestRole")
            .assumedBy(ServicePrincipal("lambda.amazonaws.com"))
            .build()
        val queue = Queue.Builder.create(stack, "TestQueue").build()

        // Act
        LambdaFunctionInfra(stack, role, queue)

        // Assert
        val template = Template.fromStack(stack)
        template.hasResourceProperties(
            "AWS::Lambda::Function",
            mapOf(
                "Runtime" to "java21",
                "Handler" to "com.davinchicoder.Handler::handleRequest",
                "Role" to Match.anyValue()
            )
        )
    }

    @Test
    fun `test SQS is configured as event source for Lambda`() {
        // Arrange
        val stack = Stack()

        val role = Role.Builder.create(stack, "TestRole")
            .assumedBy(ServicePrincipal("lambda.amazonaws.com"))
            .build()
        val queue = Queue.Builder.create(stack, "TestQueue").build()

        // Act
        LambdaFunctionInfra(stack, role, queue)

        // Assert
        val template = Template.fromStack(stack)
        template.hasResourceProperties(
            "AWS::Lambda::EventSourceMapping",
            mapOf(
                "EventSourceArn" to Match.objectLike(
                    mapOf(
                        "Fn::GetAtt" to Match.arrayWith(
                            listOf(
                                Match.stringLikeRegexp("TestQueue.*"),
                                "Arn"
                            )
                        )
                    )
                ),
                "BatchSize" to 10
            )
        )
    }

    @Test
    fun `test Lambda function has correct memory size`() {
        // Arrange
        val stack = Stack()
        val role = Role.Builder.create(stack, "TestRole")
            .assumedBy(ServicePrincipal("lambda.amazonaws.com"))
            .build()
        val queue = Queue.Builder.create(stack, "TestQueue").build()

        // Act
        LambdaFunctionInfra(stack, role, queue)

        // Assert
        val template = Template.fromStack(stack)
        template.hasResourceProperties(
            "AWS::Lambda::Function",
            mapOf(
                "MemorySize" to 1024
            )
        )
    }

    @Test
    fun `test Lambda function has correct timeout`() {
        // Arrange
        val stack = Stack()
        val role = Role.Builder.create(stack, "TestRole")
            .assumedBy(ServicePrincipal("lambda.amazonaws.com"))
            .build()
        val queue = Queue.Builder.create(stack, "TestQueue").build()

        // Act
        LambdaFunctionInfra(stack, role, queue)

        // Assert
        val template = Template.fromStack(stack)
        template.hasResourceProperties(
            "AWS::Lambda::Function",
            mapOf(
                "Timeout" to 10
            )
        )
    }
}