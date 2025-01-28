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
    val jwtSecret: String get() = System.getenv("JWT_SECRET")
}