package com.tomato.plugins

import com.tomato.auth.JwtConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

val jwtConfig = JwtConfig("123")

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt {
            jwtConfig.configureKtorFeature(this)
        }
    }

}
