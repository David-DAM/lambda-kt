package com.davinchicoder

import software.amazon.awscdk.App

class Infra {

    fun main() {
        val app = App()

        InfraStack(app, "InfraStack")

        app.synth()
    }

}