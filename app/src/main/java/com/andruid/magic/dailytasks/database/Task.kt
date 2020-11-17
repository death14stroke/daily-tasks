package com.andruid.magic.dailytasks.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val title: String,
    val repeat: Boolean = false,
    val time: Long,
    val category: String,
    val status: String = "Upcoming"
)