package com.al

import java.util.*

class Config {
    private val properties = Properties()

    init {
        val inputStream = javaClass.classLoader.getResourceAsStream("config.properties")
            ?: throw IllegalStateException("config.properties not found")
        properties.load(inputStream)
    }

    // Database configuration
    val dbUrl: String get() = properties.getProperty("db.url")
    val dbDriver: String get() = properties.getProperty("db.driver")
    val dbUser: String get() = properties.getProperty("db.user")
    val dbPassword: String get() = properties.getProperty("db.password")

    // JWT configuration
    val jwtSecret: String get() = properties.getProperty("jwt.secret")
    val jwtIssuer: String get() = properties.getProperty("jwt.issuer")
    val jwtAudience: String get() = properties.getProperty("jwt.audience")

    // Firebase configuration
    val firebaseCredentialsPath: String get() = properties.getProperty("firebase.credentials.path")
}