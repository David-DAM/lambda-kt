package com.davinchicoder

import org.junit.Test
import software.amazon.awscdk.App
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InfraKtTest {

    @Test
    fun `test main function initializes App`() {
        // Arrange & Act
        val app = App()
        val stack = InfraStack(app, "InfraStack")

        app.synth()

        // Assert
        assertNotNull(app, "App should not be null")
        assertNotNull(stack, "InfraStack should be created successfully")
    }

    @Test
    fun `test App synthesizes successfully`() {
        // Arrange
        val app = App()

        // Act
        InfraStack(app, "InfraStack")
        val cloudAssembly = app.synth()

        // Assert
        assertNotNull(cloudAssembly, "Cloud Assembly should not be null")
        assertTrue(
            cloudAssembly.artifacts.isNotEmpty(),
            "Cloud Assembly should contain artifacts after synthesis"
        )
    }
}