package com.andruid.magic.dailytasks.database

import android.app.Application

object TaskRepository {
    private lateinit var database: TaskDatabase

    fun init(application: Application) {
        database = TaskDatabase(application.applicationContext)
    }

    suspend fun insertTask(task: Task) {
        database.taskDao().insertTask(task)
    }

    fun getTasks() = database.taskDao().getTasks()

    suspend fun updateTaskStatus(id: Long, status: String) {
        database.taskDao().updateStatus(id, status)
    }
}