package com.al

import io.ktor.client.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.util.reflect.*
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import javax.security.sasl.AuthenticationException
import com.al.Config

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {

}


