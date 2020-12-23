package com.andruid.magic.dailytasks.manager

import android.util.Log
import com.andruid.magic.dailytasks.data.STATUS_DONE
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.util.getMidnightTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import java.util.concurrent.TimeUnit
import kotlin.math.max

object StatisticsManager {
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
            val diffMillis = max(currentTime - (fromTime ?: currentTime), 0)
            val days = TimeUnit.MILLISECONDS.toDays(diffMillis)

            Log.d("statsLog", "tasks = $tasks, days = $days")

            try {
                (tasks / days).toInt()
            } catch (e: ArithmeticException) {
                0
            }
        }
    }

    fun calculateTimePerTask(): Flow<Long> {
        return TaskRepository.getTaskElapsedTimes().map { elapsedTimes ->
            val count = elapsedTimes.size
            val totalTime = elapsedTimes.map { time -> max(time, 0) }.sum()

            try {
                totalTime / count
            } catch (e: ArithmeticException) {
                e.printStackTrace()
                0
            }
        }
    }
}