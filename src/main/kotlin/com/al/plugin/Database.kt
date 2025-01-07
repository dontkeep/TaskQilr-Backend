package com.al.plugin

import com.al.model.exposemodel.SessionsTable
import com.al.model.exposemodel.UsersTable
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.databaseConfiguration() {
    val jdbcUrl = "jdbc:mysql://localhost:3306/taskqilr"
    val driverClass = "com.mysql.cj.jdbc.Driver"
    val user = "root" //
    val password = "1234"

    Database.connect(jdbcUrl, driverClass, user, password)
    setupDatabase()
}

fun setupDatabase() {
    transaction {
        // Create or update tables
        SchemaUtils.createMissingTablesAndColumns(UsersTable, SessionsTable)
    }
}