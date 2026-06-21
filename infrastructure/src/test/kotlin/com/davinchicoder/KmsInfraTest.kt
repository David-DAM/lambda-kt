package com.davinchicoder

import org.junit.Test
import software.amazon.awscdk.Stack
import software.amazon.awscdk.assertions.Template

/**
 * Unit tests for the KmsInfra class.
 *
 * KmsInfra is responsible for creating a KMS Key within an AWS CDK Stack.
 * The tests verify that the KMS Key is created with the correct properties
 * when the initializeResources function is called.
 */
class KmsInfraTest {

    @Test
    fun `test initializeResources creates KMS Key`() {
        // Arrange
        val stack = Stack()

        // Act
        KmsInfra(stack)
        val template = Template.fromStack(stack)

        // Assert
        template.hasResourceProperties(
            "AWS::KMS::Key", mapOf(
                "KeyPolicy" to mapOf<String, Any>(),
                "Description" to "key-description",
                "EnableKeyRotation" to false
            )
        )
    }
}