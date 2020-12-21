package com.andruid.magic.dailytasks.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andruid.magic.dailytasks.data.STATUS_PENDING
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val title: String,
    val repeat: Boolean = false,
    val startTime: Long,
    val endTime: Long = -1,
    val day: Int,
    val month: Int,
    val year: Int,
    val category: String,
    val status: Int = STATUS_PENDING
) : Parcelable