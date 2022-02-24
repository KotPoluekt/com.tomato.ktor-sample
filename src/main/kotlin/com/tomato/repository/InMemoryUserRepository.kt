package com.tomato.repository

class InMemoryUserRepository : UserRepository {

    private var credentialsToUsers = mapOf<String, UserRepository.User>(
        "admin:admin" to UserRepository.User(1, "admin"),
        "max:1234" to UserRepository.User(2, "max")
    )

    override fun getUser(username: String, password: String): UserRepository.User? {
        return credentialsToUsers["$username:$password"]
    }
}