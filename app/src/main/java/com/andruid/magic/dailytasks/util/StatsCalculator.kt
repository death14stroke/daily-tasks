package com.andruid.magic.dailytasks.util

import com.andruid.magic.dailytasks.data.STATUS_DONE
import com.andruid.magic.dailytasks.database.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import java.util.concurrent.TimeUnit

object StatsCalculator {
    fun calculateDailyProgress(): Flow<Int> {
        val fromMillis = getMidnightTimeMillis()
        val toMillis = getMidnightTimeMillis(1)

        val totalTasks = TaskRepository.getTotalTasksCount(fromMillis, toMillis)
        val doneTasks = TaskRepository.getStatusTasksCount(STATUS_DONE, fromMillis, toMillis)

        return totalTasks.zip(doneTasks) { total, done ->
            try {
                done * 100 / total
            } catch (e: ArithmeticException) {
                0
            }
        }
    }

    fun calculateTasksPerDay(): Flow<Int> {
        val fromMillis = TaskRepository.getTaskHistoryStartTimeFlow()
        val noOfTasks = TaskRepository.getTotalTasksCount()

        return noOfTasks.zip(fromMillis) { tasks, fromTime ->
            val currentTime = System.currentTimeMillis()
            val diffMillis = currentTime - (fromTime ?: currentTime)
            val days = TimeUnit.MILLISECONDS.toDays(diffMillis)

            try {
                (tasks / days).toInt()
            } catch (e: ArithmeticException) {
                0
            }
        }
    }
}