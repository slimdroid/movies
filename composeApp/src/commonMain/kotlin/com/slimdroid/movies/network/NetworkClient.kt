package com.slimdroid.movies.network

import com.slimdroid.movies.BuildConfig.TMDB_TOKEN
import com.slimdroid.movies.logInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object NetworkClient {

    private const val HOST = "api.themoviedb.org"

    private val json by lazy {
        Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
    }

    val httpClient: HttpClient by lazy {
        HttpClient(getHttpEngine()) {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = HOST
                }
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                header("Authorization", "Bearer $TMDB_TOKEN")
            }

            install(ContentNegotiation) {
                json(json)
            }
            install(Logging) {
//                logger = Logger.ANDROID
                logger = object : Logger {
                    override fun log(message: String) {
                        logInfo("HTTP_Client", message)
                    }
                }
                level = LogLevel.BODY
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 5_000
                requestTimeoutMillis = 5_000
                connectTimeoutMillis = 2_000
            }
            HttpResponseValidator {
                handleResponseExceptionWithRequest { cause, _ ->
                    val clientException = cause as? ClientRequestException
                        ?: return@handleResponseExceptionWithRequest
                    val exceptionResponse = clientException.response
                    val error: Error = exceptionResponse.body()
                    logInfo(
                        "NetworkClient",
                        "HTTP Error ${error.statusCode} ${error.statusMessage}"
                    )
//                    co.touchlab.kermit.Logger.e {"HTTP Error ${error.statusCode} ${error.statusMessage}" }
                    throw HttpClientException(error)
                }
            }
        }
    }
}

expect fun getHttpEngine(): HttpClientEngine

@Serializable
data class Error(
    @SerialName("status_code") val statusCode: Int,
    @SerialName("status_message") val statusMessage: String,
    @SerialName("success") val success: Boolean
)

data class HttpClientException(val error: Error) :
    IllegalStateException("HTTP Error ${error.statusCode}")
