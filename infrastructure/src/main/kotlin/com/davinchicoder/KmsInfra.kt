package com.davinchicoder

import software.amazon.awscdk.Duration
import software.amazon.awscdk.RemovalPolicy
import software.amazon.awscdk.Stack
import software.amazon.awscdk.services.kms.IKey
import software.amazon.awscdk.services.kms.Key

class KmsInfra(val stack: Stack) {

    lateinit var kmsKey: IKey

    init {
        initializeResources()
    }

    fun initializeResources() {
        kmsKey = Key.Builder.create(stack, "encryption-key")
            .alias("key-alias")
            .enableKeyRotation(false)
            .pendingWindow(Duration.days(7))
            .removalPolicy(RemovalPolicy.DESTROY)
            .description("key-description")
            .build()

    }

}