package com.davinchicoder

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse
import com.amazonaws.services.lambda.runtime.events.SQSEvent
import org.slf4j.LoggerFactory

class Handler : RequestHandler<SQSEvent, SQSBatchResponse> {

    companion object {
        private val log = LoggerFactory.getLogger(Handler::class.java)
    }

    override fun handleRequest(event: SQSEvent, context: Context): SQSBatchResponse {
        log.info("Handling request with number of records: ${event.records.size}")
        val batchItemFailures = mutableListOf<SQSBatchResponse.BatchItemFailure>()

        event.records.forEach { record ->
            log.info("Processing record: ${record.body}")
        }

        return SQSBatchResponse(batchItemFailures)
    }
}