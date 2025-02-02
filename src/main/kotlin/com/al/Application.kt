package com.al

import io.ktor.client.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.util.reflect.*
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import javax.security.sasl.AuthenticationException
import com.al.Config
import com.al.features.auth.AuthController
import com.al.features.session.SessionDao
import com.al.features.user.UserDao
import com.al.helper.LocalDateTimeAdapter
import com.al.plugins.DatabaseFactory
import com.al.plugins.configureSecurity
import com.al.routes.authRoutes
import com.al.routes.protectedRoutes
import io.ktor.http.content.*
import io.ktor.serialization.gson.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.cors.routing.*
import java.text.DateFormat
import java.time.LocalDateTime

fun main(args: Array<String>) {
    val jwtSecret = System.getenv("JWT_SECRET")
    println("JWT_SECRET from environment: $jwtSecret")
    if (jwtSecret.isNullOrEmpty()) {
        throw IllegalStateException("JWT_SECRET is not set in the environment variables.")
    }
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()

    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            serializeNulls()
            registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        }
    }


    install(CORS) {
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
        methods.addAll(HttpMethod.DefaultMethods)
        headers.addAll(listOf(HttpHeaders.Authorization, HttpHeaders.ContentType))
    }

    val userDao = UserDao()
    val sessionDao = SessionDao()
    val authController = AuthController(userDao, sessionDao)

    configureSecurity(sessionDao)

    routing {
        authRoutes(authController)

        authenticate("jwt-auth") {
            route("/auth") {
                post("/logout") {
                    val principal = call.principal<JWTPrincipal>()
                    val tokenId = principal?.payload?.getClaim("jti")?.asString()
                        ?: throw IllegalStateException("Token ID not found in JWT")

                    authController.logout(tokenId)
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Logged out successfully"))
                }
            }
            protectedRoutes()
        }
    }
}


