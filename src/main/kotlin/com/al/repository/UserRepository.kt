package com.al.repository

import com.al.model.exposemodel.UsersTable
import com.al.model.fromdb.User
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
class UserRepository {

    // Create or update user
    fun createUser(user: User): User {
        val userId = transaction {
            UsersTable.insertAndGetId {
                it[name] = user.name
                it[email] = user.email
                it[username] = user.username ?: ""
                it[fcmToken] = user.fcmToken
                it[createdAt] = CurrentDateTime
            }
        }
        return user.copy(id = userId.value)
    }


    // Find user by email
    fun findByEmail(email: String): User? {
        return transaction {
            UsersTable.selectAll()
                .where {UsersTable.email.eq(email)}
                .mapNotNull { row ->
                    User(
                        id = row[UsersTable.id].value, // Extract the ID from the EntityID
                        name = row[UsersTable.name],
                        email = row[UsersTable.email],
                        username = row[UsersTable.username],
                        fcmToken = row[UsersTable.fcmToken]
                    )
                }.singleOrNull()
        }
    }

    // Update the FCM token
    fun updateFcmToken(email: String, fcmToken: String) {
        transaction {
            UsersTable.update({ UsersTable.email eq email }) {
                it[UsersTable.fcmToken] = fcmToken
            }
        }
    }

    fun getUserById(email: String): User? {
        return transaction {
            UsersTable.selectAll()
                .where { UsersTable.email eq email }
                .mapNotNull { row ->
                    User(
                        id = row[UsersTable.id].value, // Extract the ID from the EntityID
                        name = row[UsersTable.name],
                        email = row[UsersTable.email],
                        username = row[UsersTable.username],
                        fcmToken = row[UsersTable.fcmToken]
                    )
                }.singleOrNull()
        }
    }
}