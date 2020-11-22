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

    suspend fun updateTaskStatus(id: Long, status: Int) {
        database.taskDao().updateStatus(id, status)
    }

    fun getTotalTasksCount(fromMillis: Long, toMillis: Long) =
        database.taskDao().getTotalTasksCount(fromMillis, toMillis)

    fun getStatusTasksCount(status: Int, fromMillis: Long, toMillis: Long) =
        database.taskDao().getStatusTasksCount(status, fromMillis, toMillis)

    fun getStatusTasksCount(status: Int) =
        database.taskDao().getStatusTasksCount(status)

    fun searchCompletedTasks(query: String) =
        database.taskDao().searchCompletedTasks(query)
}