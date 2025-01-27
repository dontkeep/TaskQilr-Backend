package com.al.routes

import com.al.features.auth.AuthController
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Route.authRoutes(authController: AuthController) {
    route("/auth") {
        post("/login") {
            try {
                val idToken = when (call.request.contentType()) {
                    ContentType.Application.FormUrlEncoded -> {
                        call.receiveParameters()["idToken"]
                    }
                    ContentType.MultiPart.FormData -> {
                        var token: String? = null
                        val multipart = call.receiveMultipart()
                        multipart.forEachPart { part ->
                            if (part is PartData.FormItem && part.name == "idToken") {
                                token = part.value
                            }
                            part.dispose()
                        }
                        token
                    }
                    else -> throw UnsupportedMediaTypeException(call.request.contentType())
                }

                if (idToken.isNullOrEmpty()) {
                    throw IllegalArgumentException("idToken is required")
                }

                val authResponse = authController.authenticate(idToken)
                call.respond(authResponse)
            } catch (e: Exception) {
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = mapOf("error" to "Authentication failed: ${e.message}")
                )
            } catch (e: Exception) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = mapOf("error" to "Authentication failed: ${e.message}")
                )
            }
        }
    }
}

fun Route.protectedRoutes() {
    authenticate("jwt-auth") {
        get("/me") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.subject
                ?: throw IllegalStateException("User ID not found in JWT")

            call.respond(
                mapOf(
                    "userId" to userId,
                    "message" to "You accessed a protected route!"
                )
            )
        }
    }
}

