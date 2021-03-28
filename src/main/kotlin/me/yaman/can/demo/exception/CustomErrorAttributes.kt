package me.yaman.can.demo.exception

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException

@Component
class CustomErrorAttributes(val objectMapper: ObjectMapper) : DefaultErrorAttributes() {
    @Value("\${spring.application.name:}")
    private lateinit var serviceName: String

    override fun getErrorAttributes(request: ServerRequest?, options: ErrorAttributeOptions?): MutableMap<String, Any> {
        val errorAttributes = super.getErrorAttributes(request, options)
        when (val error = getError(request)) {
            is ResponseStatusException -> {
                errorAttributes["description"] = error.localizedMessage
                errorAttributes["service"] = serviceName
            }
            is WebClientResponseException -> {
                errorAttributes["error"] = error.statusCode.reasonPhrase
                errorAttributes["status"] = error.statusCode.value()
                val jsonMessage: JsonNode? = try {
                    objectMapper.readTree(error.responseBodyAsString)
                } catch (ex: Exception) {
                    null
                }
                errorAttributes["message"] = jsonMessage?.get("message")?.textValue() ?: error.message
                errorAttributes["service"] = jsonMessage?.get("service")?.textValue() ?: serviceName
                errorAttributes["description"] = "WebClient Response Exception"
            }
            else -> errorAttributes["service"] = serviceName
        }
        return errorAttributes
    }
}
