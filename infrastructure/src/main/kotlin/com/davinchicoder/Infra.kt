package com.davinchicoder

import software.amazon.awscdk.App

fun main() {
    val app = App()

    InfraStack(app, "InfraStack")

    app.synth()
}