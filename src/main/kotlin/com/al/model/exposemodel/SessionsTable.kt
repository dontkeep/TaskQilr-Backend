package com.al.model.exposemodel

import jdk.jfr.Timestamp
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object SessionsTable : IntIdTable() {
    val token = varchar("token", 512) // Store session token
    val userId = reference("user_id", UsersTable.id) // Reference to Users table
    val createdAt = datetime("created_at") // Store session creation timestamp
}