package com.al.features.user

import java.time.LocalDateTime

data class User(
    val id: String,
    val firebaseUid: String,
    val email: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

data class AuthResponse(
    val token: String,
    val user: User
)