package com.davinchicoder

import software.amazon.awscdk.App
import software.amazon.awscdk.Environment
import software.amazon.awscdk.StackProps

fun main() {
    val app = App()

    data class DeploymentConfig(
        val environment: String,
        val region: String,
        val account: String?
    )

    val deployments = listOf(
        DeploymentConfig("DEV", "eu-west-3", null)
    )

    deployments.forEach { deployment ->
        val envBuilder = Environment.builder()
            .region(deployment.region)

        deployment.account?.let { envBuilder.account(it) }

        val stackProps = StackProps.builder()
            .env(envBuilder.build())
            .crossRegionReferences(false)
            .build()

        InfraStack(
            app,
            "lambda-consumer-${deployment.environment}-${deployment.region}",
            stackProps
        )
    }

    app.synth()
}