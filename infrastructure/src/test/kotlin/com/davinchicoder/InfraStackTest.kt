package com.davinchicoder

import org.junit.Test
import software.amazon.awscdk.App
import software.amazon.awscdk.assertions.Template
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InfraStackTest {

    @Test
    fun `test initializeResources creates KMS key`() {
        val resources = getResources()
        val kmsKey = resources.values.find { (it as Map<*, *>)["Type"] == "AWS::KMS::Key" }
        assertNotNull(kmsKey, "KMS Key should exist in the stack")
    }

    @Test
    fun `test initializeResources creates SQS queue with DLQ configuration`() {
        val resources = getResources()
        val sqsQueue =
            resources.values.find { (it as Map<*, *>)["Type"] == "AWS::SQS::Queue" && it["Properties"].let { props -> (props as Map<*, *>)["QueueName"] == "SqsQueue" } }
        assertNotNull(sqsQueue, "SQS Queue should exist in the stack")

        val dlq =
            resources.values.find { (it as Map<*, *>)["Type"] == "AWS::SQS::Queue" && it["Properties"].let { props -> (props as Map<*, *>)["QueueName"] == "SqsDlq" } }
        assertNotNull(dlq, "Dead Letter Queue should exist in the stack")
    }

    @Test
    fun `test initializeResources creates Lambda function`() {

        // Assert
        val resources = getResources()
        val lambdaFunction = resources.values.find { (it as Map<*, *>)["Type"] == "AWS::Lambda::Function" }
        assertNotNull(lambdaFunction, "Lambda function should exist in the stack")

        val lambdaProps = (lambdaFunction as Map<*, *>)["Properties"] as Map<*, *>
        assertEquals("LambdaFunction", lambdaProps["FunctionName"], "Lambda function name should match")
        assertEquals(1024, lambdaProps["MemorySize"], "Lambda memory size should match")
    }

    @Test
    fun `test initializeResources creates Lambda role`() {
        val resources = getResources()
        val lambdaRole = resources.values.find { (it as Map<*, *>)["Type"] == "AWS::IAM::Role" }
        assertNotNull(lambdaRole, "Lambda IAM Role should exist in the stack")
    }

    @Test
    fun `test stack synthesizes successfully`() {
        // Arrange
        val app = App()
        val stack = InfraStack(app, "InfraStack")

        // Act
        stack.initializeResources()
        val synthesizedApp = app.synth()

        // Assert
        assertNotNull(synthesizedApp, "Application should synthesize successfully")
        assertTrue(
            synthesizedApp.stacks.isNotEmpty(),
            "Synthesized application should contain at least one stack"
        )
    }

    fun getResources(): Map<*, *> {
        val app = App()
        val stack = InfraStack(app, "InfraStack")
        stack.initializeResources()
        val template = Template.fromStack(stack)
        return template.toJSON()["Resources"] as Map<*, *>
    }
}