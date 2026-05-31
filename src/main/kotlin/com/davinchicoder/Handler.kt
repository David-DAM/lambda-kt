package com.davinchicoder

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import org.slf4j.LoggerFactory

class Handler : RequestHandler<Map<String, Any>, String> {

    companion object {
        private val log = LoggerFactory.getLogger(Handler::class.java)
    }

    override fun handleRequest(input: Map<String, Any>, context: Context): String {
        log.info("Handling request with input: $input")
        return "Hello, World!"
    }
}