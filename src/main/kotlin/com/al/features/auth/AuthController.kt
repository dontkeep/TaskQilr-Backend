package com.al.features.auth

import com.al.Config
import com.al.features.session.SessionDao
import com.al.features.user.AuthResponse
import com.al.features.user.User
import com.al.features.user.UserDao
import com.al.plugins.GoogleOAuth
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import java.time.LocalDateTime
import java.util.*

class AuthController(private val userDao: UserDao, private val sessionDao: SessionDao) {
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

        val tokenId = UUID.randomUUID().toString()
        val expiresAt = LocalDateTime.now().plusHours(1)
        val token = JWT.create()
            .withSubject(user.id)
            .withIssuer("al")
            .withAudience("528000822377-iltp1u0825hvkphc0q1p9gfoaqlrip66.apps.googleusercontent.com")
            .withClaim("jti", tokenId)
            .withExpiresAt(Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hour
            .sign(Algorithm.HMAC256(Config().jwtSecret))

        sessionDao.createSession(tokenId, user.id, expiresAt)

        return AuthResponse(token, user)
    }

    suspend fun logout(tokenId: String) {
        logger.info("tokenId: $tokenId")
        sessionDao.deactivateSession(tokenId)
    }
}

class AuthenticationException(message: String) : Exception(message)