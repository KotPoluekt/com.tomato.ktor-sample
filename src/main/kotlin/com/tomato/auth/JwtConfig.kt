package com.tomato.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

class JwtConfig(jwtSecret: String) {
    companion object Constants {
        // jwt config
        private const val jwtIssuer = "com.tomato"
        private const val jwtRealm = "com.tomato.fin"

        // claims
        private const val CLAIM_USERID = "userid"
        private const val CLAIM_USERNAME = "username"
    }

    private val jwtAlgorithm = Algorithm.HMAC256(jwtSecret)
    private val jwtVerifier: JWTVerifier = JWT
        .require(jwtAlgorithm)
        .withIssuer(jwtIssuer)
        .build()

    // generate a token for a authenticated user
    fun generateToken(user: JwtUser): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(jwtIssuer)
        .withClaim(CLAIM_USERID, user.userId)
        .withClaim(CLAIM_USERNAME, user.userName)
        .sign(jwtAlgorithm)

    // Configure the jwt authentication feature
    fun configureKtorFeature(config: JWTAuthenticationProvider.Configuration) = with(config) {
        verifier(jwtVerifier)
        realm = jwtRealm
        validate {
            val userId = it.payload.getClaim(CLAIM_USERID).asInt()
            val userName = it.payload.getClaim(CLAIM_USERNAME).asString()

            if (userId != null && userName != null) {
                JwtUser(userId, userName)
            } else {
                null
            }
        }
    }

    data class JwtUser(val userId: Int, val userName: String): Principal

}