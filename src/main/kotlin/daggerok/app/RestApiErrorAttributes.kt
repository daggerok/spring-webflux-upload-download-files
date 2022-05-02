package daggerok.app

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

/**
 * This component customizes error response.
 *
 * Adds `api` map with supported endpoints
 */
@Component
class RestApiErrorAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(request: ServerRequest?, options: ErrorAttributeOptions?): MutableMap<String, Any> =
        super.getErrorAttributes(request, options).apply {
            val baseUrl = request?.uri()?.let { "${it.scheme}://${it.authority}" } ?: ""
            val api = mapOf(
                "Get all uploads => GET" to "$baseUrl/api/v1/uploads",
                "Upload file => POST" to "$baseUrl/api/v1/upload",
                "Download file => POST" to "$baseUrl/api/v1/download/{filepath}",
            )
            put("api", api)
        }
}
