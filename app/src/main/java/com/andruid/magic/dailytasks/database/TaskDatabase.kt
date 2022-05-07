package com.andruid.magic.dailytasks.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    companion object {
        private lateinit var database: TaskDatabase
        private val LOCK = Any()

        operator fun invoke(context: Context): TaskDatabase {
            synchronized(LOCK) {
                if (!::database.isInitialized)
                    database = buildDatabase(context)
            }

            return database
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, TaskDatabase::class.java, "daily-tasks.db")
                .build()
    }

    abstract fun taskDao(): TaskDao
}