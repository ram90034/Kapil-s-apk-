package com.example.data

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ChatMessage(val text: String, val isUser: Boolean, val timestamp: Long = System.currentTimeMillis())

object GeminiDietService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    suspend fun getDietRecommendation(
        weightKg: Double,
        age: Int,
        goal: String, // "Weight Gain" or "Weight Loss"
        chatHistory: List<ChatMessage>
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "API Key is missing or is set to a placeholder. Please configure your GEMINI_API_KEY in the Secrets panel."
        }

        val systemInstruction = """
            You are a professional Old School bodybuilding and strength nutritionist. 
            The user wants advice. Given their body weight, age, and target goal (Weight Gain or Weight Loss), generate a highly detailed and clean, high-protein meal blueprint.
            Focus on whole food staples like eggs, chicken breasts, sweet potatoes, oats, beef, rice, and whole milk.
            Include:
            1. Daily Estimated Calorie & Macro Target.
            2. Structured Meal Timing (Breakfast, Lunch, Pre/Post Workout, Dinner).
            3. Pure, unfiltered motivation.
            Keep explanations concise, bulleted, clean, and highly encouraging of old-school heavy training.
        """.trimIndent()

        // Append historic conversations properly
        val contentsJson = StringBuilder()
        contentsJson.append("[")
        if (chatHistory.isNotEmpty()) {
            chatHistory.forEachIndexed { index, msg ->
                val role = if (msg.isUser) "user" else "model"
                val escapedText = escapeJson(msg.text)
                contentsJson.append("{\"role\":\"$role\",\"parts\":[{\"text\":\"$escapedText\"}]}")
                if (index < chatHistory.lastIndex) {
                    contentsJson.append(",")
                }
            }
        } else {
            val seedPrompt = "Suggest a custom nutrition diet plan for a weight target. My current body weight is $weightKg kg (approximately ${(weightKg * 2.20462).toInt()} lbs), age is $age years, and my goal is $goal."
            val escapedSeed = escapeJson(seedPrompt)
            contentsJson.append("{\"role\":\"user\",\"parts\":[{\"text\":\"$escapedSeed\"}]}")
        }
        contentsJson.append("]")

        val jsonRequest = """
            {
               "contents": $contentsJson,
               "systemInstruction": {
                   "parts": [{"text": "${escapeJson(systemInstruction)}"}]
               },
               "generationConfig": {
                   "temperature": 0.7
               }
            }
        """.trimIndent()

        val body = jsonRequest.toRequestBody("application/json".toMediaType())
        var lastErrorDetail = ""

        // Cycle through highly-resilient candidate models to bypass any localized outages (e.g. 503 Service Unavailable)
        val modelCandidates = listOf("gemini-3.5-flash", "gemini-2.5-flash", "gemini-2.5-flash-lite")
        for (modelName in modelCandidates) {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey"
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val bodyString = response.body?.string() ?: return@withContext "Error: Received empty response from core services."
                        
                        val type = com.squareup.moshi.Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
                        val adapter = moshi.adapter<Map<String, Any>>(type)
                        val rootMap = adapter.fromJson(bodyString)
                        
                        val candidates = rootMap?.get("candidates") as? List<*>
                        val firstCandidate = candidates?.firstOrNull() as? Map<*, *>
                        val content = firstCandidate?.get("content") as? Map<*, *>
                        val parts = content?.get("parts") as? List<*>
                        val firstPart = parts?.firstOrNull() as? Map<*, *>
                        val responseText = firstPart?.get("text") as? String
                        
                        if (responseText != null) {
                            return@withContext responseText
                        } else {
                            lastErrorDetail = "No text in payload for $modelName"
                        }
                    } else {
                        lastErrorDetail = "HTTP Code ${response.code} for $modelName"
                    }
                }
            } catch (e: IOException) {
                lastErrorDetail = "Network connection timed out: ${e.localizedMessage} with $modelName"
            } catch (e: Exception) {
                lastErrorDetail = "Parsing or execution error: ${e.localizedMessage} with $modelName"
            }
        }

        "Error: Direct REST API connection failed ($lastErrorDetail). Please confirm your key is valid and has Gemini access."
    }

    private fun escapeJson(input: String): String {
        return input.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
}
