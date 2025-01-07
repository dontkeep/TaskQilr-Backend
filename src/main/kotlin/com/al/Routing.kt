package com.al

import com.al.client.GoogleAuthService
import com.al.client.MLService
import com.al.model.fromres.UserSession
import com.al.repository.SessionRepository
import com.al.repository.UserRepository
import com.al.routes.loginRoute
import com.al.routes.mlRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.configureRouting(mlService: MLService, googleAuthService: GoogleAuthService, userRepository: UserRepository, sessionRepository: SessionRepository) {
    install(Sessions) {
        cookie<UserSession>("user_session") // Store sessions in a browser cookie
    }

    routing {
        loginRoute(googleAuthService, userRepository, sessionRepository)
        get("/") {
            call.respondText("Welcome! Please log in at /login")
        }

        get("/home") {
            val userSession: UserSession? = call.sessions.get()
            if (userSession != null) {
                val userInfo = googleAuthService.fetchUserInfo(userSession.token)
                call.respondText("Hello, ${userInfo.name}! Welcome home!")

            } else {
                call.respondRedirect("/login")
            }
        }
        mlRoutes(mlService)
    }
}
