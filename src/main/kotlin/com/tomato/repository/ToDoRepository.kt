package com.tomato.repository

import com.tomato.entities.ToDo
import com.tomato.entities.ToDoDraft

interface ToDoRepository {

    fun getAllTodos(): List<ToDo>

    fun getTodo(id: Int): ToDo?

    fun addToDo(draft: ToDoDraft): ToDo

    fun removeToDo(id: Int): Boolean

    fun updateToDo(id: Int, draft: ToDoDraft): Boolean
}