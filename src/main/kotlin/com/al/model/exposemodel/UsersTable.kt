package com.al.model.exposemodel

import com.google.protobuf.Timestamp
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object UsersTable : IntIdTable() {
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255)
    val fcmToken = varchar("fcm_token", 255).nullable()
    val createdAt = datetime("created_at")
}