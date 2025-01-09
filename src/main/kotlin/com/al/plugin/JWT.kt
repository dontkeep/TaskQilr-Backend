package com.al.plugin

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

val jwtSecret = System.getenv("JWT_SECRET")
val jwtIssuer = "https://precious-sure-lioness.ngrok-free.app"
val jwtAudience = "https://precious-sure-lioness.ngrok-free.app"

fun Application.configureJWT() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "Access to the '/'"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withIssuer(jwtIssuer)
                    .withAudience(jwtAudience)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("id").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }
}