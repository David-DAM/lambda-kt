package com.davinchicoder

import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.constructs.Construct

class InfraStack(
    scope: Construct,
    id: String,
    props: StackProps? = null
) : Stack(scope, id, props) {

    init {
        initializeResources()
    }

    fun initializeResources() {

        val kmsInfra = KmsInfra(this)

        val lambdaRoleInfra = LambdaRoleInfra(this)

        val sqsInfra = SqsInfra(this, kmsInfra.kmsKey, lambdaRoleInfra.lambdaRole)

        LambdaFunctionInfra(this, lambdaRoleInfra.lambdaRole, sqsInfra.sqsQueue)
    }
}