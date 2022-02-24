package com.tomato.plugins

import com.tomato.auth.JwtConfig
import com.tomato.entities.LoginBody
import com.tomato.entities.ToDoDraft
import com.tomato.repository.InMemoryUserRepository
import com.tomato.repository.MySqlToDoRepository
import com.tomato.repository.ToDoRepository
import com.tomato.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Application.configureRouting() {

    routing {

        val repository: ToDoRepository = MySqlToDoRepository()
        val userRepository: UserRepository = InMemoryUserRepository()

        get("/") {
            call.respondText("Hello Fin Server!")
        }

        post("/login") {
            val loginBody = call.receive<LoginBody>()

            val user = userRepository.getUser(loginBody.username, loginBody.password)

            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                return@post
            }

            val token = jwtConfig.generateToken(JwtConfig.JwtUser(user.userId, user.username))
            call.respond(token)
        }

        authenticate {
            get("/me") {
                val user = call.authentication.principal as JwtConfig.JwtUser
                call.respond(user)
            }

            get("/todos") {
                call.respond(repository.getAllTodos())
            }

            get("/todos/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id ==null) {
                    call.respond(HttpStatusCode.BadRequest, "Id parameter has to be a number")
                    return@get
                }
                val todo = repository.getTodo(id)

                if (todo == null) {
                    call.respond(HttpStatusCode.NotFound, "Todo with id $id does not exists")
                } else {
                    call.respond(todo)
                }
            }

            post("/todos") {
                val todoDraft = call.receive<ToDoDraft>()
                val todo = repository.addToDo(todoDraft)
                call.respond(todo)
            }

            put("/todos/{id}") {
                val todoDraft = call.receive<ToDoDraft>()
                val id = call.parameters["id"]?.toIntOrNull()

                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Id parameter has to be a number")
                    return@put
                }

                val updated = repository.updateToDo(id, todoDraft)
                if (updated) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Todo with id $id does not exists")
                }
            }

            delete("/todos/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Id parameter has to be a number")
                    return@delete
                }

                val deleted = repository.removeToDo(id)
                if (deleted) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Todo with id $id does not exists")
                }
            }
        }
    }
}
