package com.tomato.repository

import com.tomato.database.DatabaseManager
import com.tomato.entities.ToDo
import com.tomato.entities.ToDoDraft

class MySqlToDoRepository: ToDoRepository {

    private val database = DatabaseManager()

    override fun getAllTodos(): List<ToDo> {
        return database.getAllToDos()
            .map { ToDo(it.id, it.title, it.done) }
    }

    override fun getTodo(id: Int): ToDo? {
        return database.getToDo(id)?.let { ToDo(it.id, it.title, it.done) }
    }

    override fun addToDo(draft: ToDoDraft): ToDo {
        return database.addToDo(draft)
    }

    override fun removeToDo(id: Int): Boolean {
        return database.deleteToDo(id)
    }

    override fun updateToDo(id: Int, draft: ToDoDraft): Boolean {
        return database.updateToDo(id, draft)
    }

}