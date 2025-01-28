package com.al.plugins

import com.al.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val config = Config()
        val pool = hikari(config)

        Database.connect(pool)

        transaction {
            SchemaUtils.create(Users, Sessions)
        }
    }

    private fun hikari(config: Config): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = config.dbDriver
            jdbcUrl = config.dbUrl
            username = config.dbUser
            password = config.dbPassword
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        return HikariDataSource(config)
    }
}

object Users : Table("users") {
    val id = varchar("id", 36)
    val firebaseUid = varchar("firebase_uid", 128).uniqueIndex()
    val email = varchar("email", 255)
    val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(id)
}

object Sessions : Table("sessions") {
    val tokenId = varchar("token_id", 36)
    val userId = varchar("user_id", 36).references(Users.id)
    val createdAt = datetime("created_at")
    val expiresAt = datetime("expires_at")
    val isActive = bool("is_active").default(true)

    override val primaryKey = PrimaryKey(tokenId)
}