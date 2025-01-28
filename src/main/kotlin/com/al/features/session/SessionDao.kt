package com.al.features.session

import com.al.plugins.Sessions
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class SessionDao {
    suspend fun createSession(tokenId: String, userId: String, expiresAt: LocalDateTime): Unit = transaction {
        Sessions.insert {
            it[Sessions.tokenId] = tokenId
            it[Sessions.userId] = userId
            it[Sessions.createdAt] = LocalDateTime.now()
            it[Sessions.expiresAt] = expiresAt
            it[Sessions.isActive] = true
        }
    }

    suspend fun isSessionActive(tokenId: String): Boolean = transaction {
        Sessions.select { (Sessions.tokenId eq tokenId) and (Sessions.isActive eq true) }
            .map { it[Sessions.expiresAt] }
            .singleOrNull()
            ?.isAfter(LocalDateTime.now()) ?: false
    }

    suspend fun deactivateSession(tokenId: String): Unit = transaction {
        Sessions.update({ Sessions.tokenId eq tokenId }) {
            it[isActive] = false
        }
    }

    suspend fun cleanupExpiredSessions(): Unit = transaction {
        Sessions.deleteWhere { Sessions.expiresAt lessEq LocalDateTime.now() }
    }
}