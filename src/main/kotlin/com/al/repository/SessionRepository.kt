package com.al.repository

import com.al.model.exposemodel.SessionsTable
import com.al.model.exposemodel.UsersTable
import com.al.model.fromdb.Session
import com.al.model.fromdb.User
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.insertIgnoreAndGetId
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Timestamp

class SessionRepository {
    fun createSession(session: Session): Session {
        val sesId = transaction {
            SessionsTable.insertAndGetId {
                it[token] = session.token
                it[userId] = session.userId
                it[createdAt] = CurrentDateTime
            }
        }
        return session.copy(id = sesId.value)
    }
}