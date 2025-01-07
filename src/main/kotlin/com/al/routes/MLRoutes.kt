package com.al.routes

import com.al.client.MLService
import com.al.model.fromres.InputInstance
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationResponse(
    val recommendedTime: Float
)

fun Route.mlRoutes(mlApi: MLService) {
    post("/recommendation") {
        // Parse input from the frontend
        val params = call.receive<InputInstance>()

        // Get recommendation from the ML API
        try {
            val recommendation = mlApi.getRecommendation(
                lastStudyTime = params.keras_tensor[0],
                studyDuration = params.keras_tensor[1]
            )
            call.respond(message = RecommendationResponse(recommendedTime = recommendation),
                typeInfo = typeInfo<RecommendationResponse>())

        } catch (e: Exception) {
            print("error occured: $e")
        }
    }
}