package com.al.client

import com.al.model.fromres.InputInstance
import com.al.model.fromres.PredictionResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import java.net.ConnectException

@Serializable
data class PredictionRequest(val instances: List<InputInstance>)

class MLService(private val client: HttpClient) {
    private val mlUrl = "http://172.29.77.186:8501/v1/models/learning_recommendation_model:predict"

    suspend fun getRecommendation(lastStudyTime: Int, studyDuration: Int): Float {
        // Create the request body
        val requestBody = PredictionRequest(
            instances = listOf(InputInstance(keras_tensor = listOf(lastStudyTime, studyDuration)))
        )
        logger.info("$requestBody")
        try {
            val response: PredictionResponse = client.post(mlUrl) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }.body()

            // Extract the prediction (first value in the first list)
            return response.predictions.firstOrNull()?.firstOrNull()
                ?: throw IllegalStateException("No prediction returned by the ML API")
        } catch (e: Exception) {
            throw IllegalStateException("Error during prediction request ", e)
        }catch (e: ConnectException) {
            throw IllegalStateException("Failed to connect to the model server", e)
        }
    }
}