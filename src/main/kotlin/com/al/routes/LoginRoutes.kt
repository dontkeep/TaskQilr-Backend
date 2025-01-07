package com.al.routes

import com.al.client.GoogleAuthService
import com.al.model.UserInfo
import com.al.model.UserSession
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.headers
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.loginRoute(httpClient: GoogleAuthService) {
    authenticate("auth-oauth-google") {
        get("/login") {
            // This automatically redirects the user to the Google login page
        }

        get("/callback") {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
            if (principal != null) {
                val userSession = UserSession(principal.state ?: "", principal.accessToken)
                call.sessions.set(userSession)

                val userInfo = httpClient.fetchUserInfo(principal.accessToken)

                call.respondText("Hello, ${userInfo.name}! Welcome to the application.")
                call.respondRedirect("/home")
            } else {
                call.respondText("Authentication failed. Please try again.")
            }
        }
    }
}
