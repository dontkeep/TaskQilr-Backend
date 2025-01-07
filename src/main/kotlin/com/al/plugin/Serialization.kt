package com.al.plugin

import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import kotlinx.serialization.json.Json


fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
                prettyPrint = true
                isLenient = true
            }
        )  // Using Kotlinx Serialization
    }
}
