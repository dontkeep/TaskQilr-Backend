package com.al.routes

import com.al.client.GoogleAuthService
import com.al.model.fromdb.Session
import com.al.model.fromdb.User
import com.al.model.fromres.UserSession
import com.al.plugin.jwtAudience
import com.al.plugin.jwtIssuer
import com.al.plugin.jwtSecret
import com.al.repository.SessionRepository
import com.al.repository.UserRepository
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import java.time.LocalDateTime
import java.util.*

fun Route.loginRoute(httpClient: GoogleAuthService, userRepository: UserRepository, sessionRepository: SessionRepository) {
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

                val existingUser = userRepository.findByEmail(userInfo.email)

                val user = if (existingUser != null) {
                    existingUser
                } else {
                    val newUser = User(
                        name = userInfo.name,
                        email = userInfo.email,
                        username = userInfo.name,
                        fcmToken = "",
                        createdAt = LocalDateTime.now()
                    )
                    userRepository.createUser(newUser)
                }

                val jwt = JWT.create()
                    .withIssuer(jwtIssuer)
                    .withAudience(jwtAudience)
                    .withClaim("id", user.id)
                    .withClaim("name", user.name)
                    .withClaim("email", user.email)
                    .sign(Algorithm.HMAC256(jwtSecret))

                val session = user.id?.let {
                    Session(
                        token = jwt,
                        userId = it,
                        createdAt = LocalDateTime.now()
                    )
                }

                if (session != null) {
                    sessionRepository.createSession(session)
                }
                val callbackUrl = "https://precious-sure-lioness.ngrok-free.app/callback?token=$jwt"
                call.respondRedirect(callbackUrl)
            } else {
                call.respondText("Authentication failed. Please try again.")
            }
        }
    }
}
