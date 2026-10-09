package com.example.miniclassroom

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable data class Message(val role: String, val content: String)
@Serializable data class AiRequest(val model: String, val messages: List<Message>)
@Serializable data class Choice(val message: Message)
@Serializable data class AiResponse(val choices: List<Choice>)

object AiApi {
    private const val API_KEY = "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"

    private val client = HttpClient(Android) {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    }

    suspend fun summarize(text: String): String {
        val response = client.post("https://openrouter.ai/api/v1/chat/completions") {
            header("Authorization", "Bearer $API_KEY")
            contentType(ContentType.Application.Json)
            setBody(
                AiRequest(
                    model = "openrouter/free",
                    messages = listOf(Message("user", "Résume ce cours en français en 3 phrases :\n\n$text"))
                )
            )
        }
        if (!response.status.isSuccess()) {
            error("HTTP ${response.status.value} : ${response.bodyAsText().take(200)}")
        }
        return response.body<AiResponse>().choices.first().message.content
    }
}