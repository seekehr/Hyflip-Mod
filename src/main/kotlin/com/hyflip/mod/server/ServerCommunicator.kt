package com.hyflip.mod.server

import kotlin.text.get
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hyflip.mod.server.ServerCommunicator.FoundFlip
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.io.Closeable
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

const val DEV_BACKEND_URL = "http://localhost:3000"
const val BACKEND_URL = "http://localhost:3000"

typealias OnFoundFlipCallback = (flip: FoundFlip) -> Unit

/**
 * Awaits an OkHttp call in a suspending way. #nomorecallbackhell
 */
suspend fun Call.await(): Response {
    return suspendCancellableCoroutine { continuation -> // continuation pauses this coroutine after smth that suspends execution is done (like await; or enqueue here) and resumes when resume is called
        enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                continuation.resume(response) // resume
            }
            override fun onFailure(call: Call, e: IOException) {
                if (continuation.isCancelled) return
                continuation.resumeWithException(e) // let someone else handle it
            }
        })
        continuation.invokeOnCancellation {
            try {
                cancel()
            } catch (ex: Throwable) {
                // was cancelled intentionally, who cares
            }
        }
    }
}

object ServerCommunicator {
    class ApiError(val status: Int, override val message: String) : Exception(message)

    // Note: No need for @Serializable with Gson
    data class ServerResponse<T>(
        val message: String,
        val data: T
    )

    // These field names match our golang field names, so this'll work just fine. additionally i could use @SerializedName
    data class FoundFlip(
        val productId: String,
        val command: String,
        val profit: Int,
        val sellPrice: Double,
        val buyPrice: Double,
        val sellVolume: Int,
        val sellMovingWeek: Int,
        val buyVolume: Int,
        val buyMovingWeek: Int,
        val recommendedFlipVolume: Int,
        val profitFromRecommendedFlipVolume: Int
    )


    val client = OkHttpClient()
    val jsonParser = Gson()

    suspend inline fun <reified T> apiFetch(urlString: String, options: Request.Builder.() -> Unit = {}): T {
        val request = Request.Builder()
            .url(urlString)
            .apply(options)
            .build()

        val response = client.newCall(request).await()

        response.use { // auto close the resource
            val body = it.body.string() // it is our response object. .use() automatically initialises it as we did not provide a receiving object for block
            if (!it.isSuccessful) {
                val errorMessage = try { // try to get the error from ServerResponse.message. if not,
                    val type = object : TypeToken<ServerResponse<Unit?>>() {}.type // we have to use this... not gonna say my mind but, we have to use it to preserve info from generics (which typically are erased at runtime in most langs) for gson
                    // object : TypeToken<ServerResponse<Unit?>>() {} is just an anonymous object declaration. and .type is well, its type derived from TypeToken.
                    val serverResponse: ServerResponse<Unit?> = jsonParser.fromJson(body, type)
                    serverResponse.message
                } catch (e: Exception) {
                    e.toString()
                }
                throw ApiError(it.code, errorMessage)
            }

            // Use TypeToken to help Gson deserialize generic types correctly
            val type = object : TypeToken<ServerResponse<T>>() {}.type
            val serverResponse: ServerResponse<T> = jsonParser.fromJson(body, type)
            return serverResponse.data
        }
    }


    suspend fun createAccount(username: String): String {
        val requestBody = mapOf("username" to username)
        val jsonBody = jsonParser.toJson(requestBody)

        val response = apiFetch<Map<String, String>>(BACKEND_URL + "create_account") {
            post(jsonBody.toRequestBody("application/json".toMediaType()))
        }

        return response["key"] ?: throw ApiError(500, "Unexpected response format. Expected an object with a \"key\" property.")
    }

    fun getBazaarFlips(token: String, username: String, onFoundFlip: OnFoundFlipCallback): Closeable {
        val request = Request.Builder()
            .url("${BACKEND_URL}api/bzflips?username=$username")
            .header("Authorization", "Bearer $token")
            .build()

        val listener = object : EventSourceListener() {
            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                try {
                    val flip: FoundFlip = jsonParser.fromJson(data, FoundFlip::class.java)
                    onFoundFlip(flip)
                } catch (e: Exception) {
                    println("Error parsing flip data: ${e.message}")
                }
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                println("EventSource failed: ${t?.message}")
                // The library will attempt to reconnect automatically
            }
        }

        val eventSource = EventSources.createFactory(client).newEventSource(request, listener)

        // Return a Closeable that cancels the EventSource. Use it to cancel over > retries
        return Closeable { eventSource.cancel() }
    }
}
