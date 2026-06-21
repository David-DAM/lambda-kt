package com.davinchicoder

import software.amazon.awscdk.Stack
import software.amazon.awscdk.services.iam.IRole
import software.amazon.awscdk.services.iam.ManagedPolicy
import software.amazon.awscdk.services.iam.Role
import software.amazon.awscdk.services.iam.ServicePrincipal

class LambdaRoleInfra(val stack: Stack) {

    lateinit var lambdaRole: IRole

    init {
        initializeResources()
    }

    fun initializeResources() {
        lambdaRole = Role.Builder.create(stack, "lambda-role")
            .assumedBy(ServicePrincipal("lambda.amazonaws.com"))
            .managedPolicies(
                listOf(
                    ManagedPolicy.fromAwsManagedPolicyName("service-role/AWSLambdaBasicExecutionRole")
                )
            )
            .build()

    }

}