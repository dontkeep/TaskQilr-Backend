package com.al

import com.al.client.GoogleAuthService
import com.al.client.MLService
import com.al.plugin.authentication
import com.al.plugin.configureSerialization
import com.al.plugin.databaseConfiguration
import com.al.repository.SessionRepository
import com.al.repository.UserRepository
import com.al.routes.mlRoutes
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import okhttp3.internal.concurrent.TaskRunner.Companion.logger

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()  // Using Kotlinx Serialization
    }
}

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    databaseConfiguration()
    val googleAuthService = GoogleAuthService(client)
    val mlService = MLService(client)
    val userRepository = UserRepository()
    val sessionRepository = SessionRepository()
    // Configure routing

    authentication(client)
    configureRouting(mlService, googleAuthService, userRepository, sessionRepository)
}


