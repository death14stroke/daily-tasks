package com.andruid.magic.dailytasks.util

import com.andruid.magic.dailytasks.data.STATUS_DONE
import com.andruid.magic.dailytasks.database.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

object StatsCalculator {
    fun calculateDailyProgress(): Flow<Int> {
        val fromMillis = getMidnightTimeMillis()
        val toMillis = getMidnightTimeMillis(1)

        val totalTasks = TaskRepository.getTotalTasksCount(fromMillis, toMillis)
        val doneTasks = TaskRepository.getStatusTasksCount(STATUS_DONE, fromMillis, toMillis)

        return totalTasks.zip(doneTasks) { total: Int, done: Int ->
            done * 100 / total
        }
    }
}