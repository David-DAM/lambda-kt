package com.davinchicoder

import software.amazon.awscdk.Duration
import software.amazon.awscdk.Stack
import software.amazon.awscdk.services.iam.IRole
import software.amazon.awscdk.services.kms.IKey
import software.amazon.awscdk.services.sqs.DeadLetterQueue
import software.amazon.awscdk.services.sqs.Queue

class SqsInfra(val stack: Stack, val kmsKey: IKey, val lambdaRole: IRole) {

    lateinit var sqsQueue: Queue

    init {
        initializeResources()
    }

    fun initializeResources() {
        val sqsDlq = Queue.Builder.create(stack, "sqs-dlq")
            .queueName("sqs-dlq")
            .deliveryDelay(Duration.seconds(10))
            .encryptionMasterKey(kmsKey)
            .fifo(false)
            .build()

        val deadLetterQueue = DeadLetterQueue.builder()
            .maxReceiveCount(5)
            .queue(sqsDlq)
            .build()

        sqsQueue = Queue.Builder.create(stack, "sqs-queue")
            .queueName("sqs-queue")
            .deliveryDelay(Duration.seconds(10))
            .encryptionMasterKey(kmsKey)
            .deadLetterQueue(deadLetterQueue)
            .fifo(false)
            .build()

        sqsQueue.grantConsumeMessages(lambdaRole)
    }

}