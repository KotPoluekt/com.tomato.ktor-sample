package com.tomato.plugins

import io.ktor.serialization.gson.*
import io.ktor.server.plugins.*
import io.ktor.server.application.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
}
