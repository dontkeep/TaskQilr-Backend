package com.al.features.auth

import com.al.Config
import com.al.features.user.AuthResponse
import com.al.features.user.User
import com.al.features.user.UserDao
import com.al.plugins.GoogleOAuth
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class AuthController(private val userDao: UserDao) {
    suspend fun authenticate(idToken: String): AuthResponse {
        val googleToken = GoogleOAuth.verifyIdToken(idToken)
            ?: throw AuthenticationException("Invalid Google OAuth token")

        val payload = googleToken.payload
        val userId = payload.subject // Unique user ID
        val email = payload.email

        val user = userDao.findByFirebaseUid(userId) ?: run {
            userDao.create(
                User(
                    id = UUID.randomUUID().toString(),
                    firebaseUid = userId,
                    email = email ?: "",
                )
            )
        }

        val token = JWT.create()
            .withSubject(user.id)
            .withIssuer("taskqilr")
            .withAudience("taskqilr")
            .withExpiresAt(Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hour
            .sign(Algorithm.HMAC256(Config().jwtSecret))

        return AuthResponse(token, user)
    }
}

class AuthenticationException(message: String) : Exception(message)