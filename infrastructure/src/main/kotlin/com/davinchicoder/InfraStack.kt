package com.davinchicoder

import software.amazon.awscdk.Duration
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.iam.ManagedPolicy
import software.amazon.awscdk.services.iam.Role
import software.amazon.awscdk.services.iam.ServicePrincipal
import software.amazon.awscdk.services.lambda.Code
import software.amazon.awscdk.services.lambda.Function
import software.amazon.awscdk.services.lambda.Runtime
import software.constructs.Construct

class InfraStack(
    scope: Construct,
    id: String,
    props: StackProps? = null
) : Stack(scope, id, props) {

    init {

        val lambdaRole = Role.Builder.create(this, "LambdaRole")
            .assumedBy(ServicePrincipal("lambda.amazonaws.com"))
            .managedPolicies(
                listOf(
                    ManagedPolicy.fromAwsManagedPolicyName("service-role/AWSLambdaBasicExecutionRole")
                )
            )
            .build()

        Function.Builder.create(this, "Lambda")
            .role(lambdaRole)
            .runtime(Runtime.JAVA_21)
            .handler("com.davinchicoder.Handler")
            .code(Code.fromAsset("../../build/libs/lambda.zip"))
            .memorySize(512)
            .timeout(Duration.seconds(10))
            .build()
    }
}