package com.davinchicoder

import org.junit.Test
import software.amazon.awscdk.Stack
import software.amazon.awscdk.assertions.Match
import software.amazon.awscdk.assertions.Template
import kotlin.test.assertNotNull

class LambdaRoleInfraTest {

    @Test
    fun `test initializeResources creates Lambda IAM Role`() {
        // Arrange
        val stack = Stack()

        // Act
        val lambdaRoleInfra = LambdaRoleInfra(stack)

        // Assert
        assertNotNull(lambdaRoleInfra.lambdaRole, "Lambda Role should be initialized")
    }

    // ... existing code ...

    @Test
    fun `test IAM Role has correct Service Principal`() {
        // Arrange
        val stack = Stack()

        // Act
        LambdaRoleInfra(stack)
        val template = Template.fromStack(stack)

        // Assert
        template.hasResourceProperties(
            "AWS::IAM::Role", mapOf(
                "AssumeRolePolicyDocument" to Match.objectLike(
                    mapOf(
                        "Statement" to Match.arrayWith(
                            listOf(
                                Match.objectLike(
                                    mapOf(
                                        "Principal" to Match.objectLike(
                                            mapOf(
                                                "Service" to "lambda.amazonaws.com"
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `test IAM Role includes AWSLambdaBasicExecutionRole managed policy`() {
        // Arrange
        val stack = Stack()

        // Act
        LambdaRoleInfra(stack)
        val template = Template.fromStack(stack)

        // Assert
        template.hasResourceProperties(
            "AWS::IAM::Role", mapOf(
                "ManagedPolicyArns" to Match.arrayWith(
                    listOf(
                        Match.objectLike(
                            mapOf(
                                "Fn::Join" to Match.arrayWith(
                                    listOf(
                                        Match.arrayWith(
                                            listOf(
                                                Match.stringLikeRegexp(".*service-role/AWSLambdaBasicExecutionRole")
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    }
}