package com.davinchicoder

import com.amazonaws.services.lambda.runtime.ClientContext
import com.amazonaws.services.lambda.runtime.CognitoIdentity
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.amazonaws.services.lambda.runtime.events.SQSEvent
import org.slf4j.LoggerFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class HandlerTest {

    @Test
    fun `test handleRequest with empty event`() {
        // Arrange
        val handler = Handler()
        val event = SQSEvent()
        event.records = emptyList()
        val context = TestContext()

        // Act
        val response = handler.handleRequest(event, context)

        // Assert
        assertNotNull(response)
        assertEquals(0, response.batchItemFailures.size)
    }

    @Test
    fun `test handleRequest with single record`() {
        // Arrange
        val handler = Handler()
        val event = SQSEvent()
        val record = SQSEvent.SQSMessage().apply {
            body = "TestMessage"
        }
        event.records = listOf(record)
        val context = TestContext()

        // Act
        val response = handler.handleRequest(event, context)

        // Assert
        assertNotNull(response)
        assertEquals(0, response.batchItemFailures.size)
    }

    @Test
    fun `test handleRequest with multiple records`() {
        // Arrange
        val handler = Handler()
        val event = SQSEvent()
        val records = listOf(
            SQSEvent.SQSMessage().apply { body = "Message1" },
            SQSEvent.SQSMessage().apply { body = "Message2" },
            SQSEvent.SQSMessage().apply { body = "Message3" }
        )
        event.records = records
        val context = TestContext()

        // Act
        val response = handler.handleRequest(event, context)

        // Assert
        assertNotNull(response)
        assertEquals(0, response.batchItemFailures.size)
    }

    // TestContext is a simple stub for the AWS Lambda Context interface.
    class TestContext : Context {
        override fun getAwsRequestId(): String = "test-request-id"
        override fun getLogGroupName(): String = "test-log-group"
        override fun getLogStreamName(): String = "test-log-stream"
        override fun getFunctionName(): String = "test-function-name"
        override fun getFunctionVersion(): String = "test-function-version"
        override fun getInvokedFunctionArn(): String = "test-function-arn"
        override fun getIdentity(): CognitoIdentity? = null
        override fun getClientContext(): ClientContext? = null
        override fun getRemainingTimeInMillis(): Int = 3000
        override fun getMemoryLimitInMB(): Int = 256
        override fun getLogger(): LambdaLogger = LoggerFactory.getLogger(TestContext::class.java) as LambdaLogger
    }
}