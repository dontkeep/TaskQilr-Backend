package com.al.features.user

import com.al.plugins.Users
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserDao {
    suspend fun findByFirebaseUid(uid: String): User? = transaction {
        Users.select { Users.firebaseUid eq uid }
            .map { row ->
                User(
                    id = row[Users.id],
                    firebaseUid = row[Users.firebaseUid],
                    email = row[Users.email],
                    createdAt = row[Users.createdAt]
                )
            }.singleOrNull()
    }

    suspend fun create(user: User): User = transaction {
        Users.insert {
            it[id] = user.id
            it[firebaseUid] = user.firebaseUid
            it[email] = user.email
            it[createdAt] = user.createdAt
        }
        user
    }
}