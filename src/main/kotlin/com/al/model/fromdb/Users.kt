package com.al.model.fromdb

import kotlinx.serialization.Contextual
import java.time.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val name: String,
    val email: String,
    val username: String?,
    val fcmToken: String?,
    @Contextual val createdAt: LocalDateTime = LocalDateTime.now()
)

@Serializable
data class Session(
    val id: Int? = null,
    val token: String,
    val userId: Int,
    @Contextual val createdAt: LocalDateTime = LocalDateTime.now()
)