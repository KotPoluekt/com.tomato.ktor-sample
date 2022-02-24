package com.tomato.database

import com.tomato.entities.ToDo
import com.tomato.entities.ToDoDraft
import org.ktorm.database.Database
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.update
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList

class DatabaseManager {
    // config
    private val hostname = "localhost"
    private val databaseName = "demo"
    private val username = "root"
    private val password = "345rtpo_90e3^5dwSE"

    // database
    private val ktormDatabase: Database

    init {
        val jdbcUrl = "jdbc:mysql://$hostname:3306/$databaseName?user=$username&password=$password&useSSL=false"
        ktormDatabase = Database.connect(jdbcUrl)
    }

    fun getAllToDos(): List<DBToDoEntity> {
        return ktormDatabase.sequenceOf(DBTodoTable).toList()
    }

    fun getToDo(id: Int): DBToDoEntity? {
        return ktormDatabase.sequenceOf(DBTodoTable)
            .firstOrNull { it.id eq id }
    }

    fun addToDo(draft: ToDoDraft): ToDo {
        val insertedId = ktormDatabase.insertAndGenerateKey(DBTodoTable) {
            set(DBTodoTable.title, draft.title)
            set(DBTodoTable.done, draft.done)
        } as Int

        return ToDo(insertedId, draft.title, draft.done)
    }

    fun updateToDo(id: Int, draft: ToDoDraft): Boolean {
        val updated = ktormDatabase.update(DBTodoTable) {
            set(DBTodoTable.title, draft.title)
            set(DBTodoTable.done, draft.done)
            where {
                it.id eq id
            }
        }

        return updated > 0
    }

    fun deleteToDo(id: Int): Boolean {
        val deleted = ktormDatabase.delete(DBTodoTable) {
            it.id eq id
        }

        return deleted > 0
    }
}