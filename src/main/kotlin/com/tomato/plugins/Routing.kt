package com.tomato.plugins

import com.tomato.entities.ToDoDraft
import com.tomato.repository.MySqlToDoRepository
import com.tomato.repository.ToDoRepository
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Application.configureRouting() {

    routing {

        val repository: ToDoRepository = MySqlToDoRepository()

        get("/") {
            call.respondText("Hello Fin Server!")
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
