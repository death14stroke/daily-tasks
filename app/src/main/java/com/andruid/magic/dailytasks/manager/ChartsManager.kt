package com.andruid.magic.dailytasks.manager

import android.util.Log
import com.andruid.magic.dailytasks.data.CATEGORY_PERSONAL
import com.andruid.magic.dailytasks.data.CATEGORY_WORK
import com.andruid.magic.dailytasks.data.Month
import com.andruid.magic.dailytasks.database.TaskRepository
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

object ChartsManager {
    suspend fun buildBarChartData(month: Month): List<BarEntry> {
        val monthlyStats = TaskRepository.getMonthlyStats(month.index, month.year) ?: emptyList()

        return withContext(Dispatchers.Default) {
            val dbData = mutableMapOf<Int, Int>()
            repeat(month.noOfDays) { day ->
                dbData[day] = 0
            }

            monthlyStats.forEach { monthStats ->
                dbData[monthStats.day - 1] = monthStats.taskCnt
            }

            dbData.map { entry -> BarEntry(entry.key.toFloat(), entry.value.toFloat()) }
        }
    }

    suspend fun buildBarChartData(weekStartMillis: Long, weekEndMillis: Long): List<BarEntry> {
        val weeklyStats =
            TaskRepository.getWeeklyStats(weekStartMillis, weekEndMillis) ?: emptyList()

        Log.d("statsLog", "weekly stats = $weeklyStats")

        return withContext(Dispatchers.Default) {
            val dbData = mutableMapOf<Int, Int>()
            repeat(7) {
                dbData[it] = 0
            }

            weeklyStats.forEach { weekStats ->
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, weekStats.day)
                    set(Calendar.MONTH, weekStats.month)
                    set(Calendar.YEAR, weekStats.year)
                }

                dbData[calendar.get(Calendar.DAY_OF_WEEK) - 1] = weekStats.taskCntWeek
            }

            dbData.map { entry -> BarEntry(entry.key.toFloat(), entry.value.toFloat()) }
        }
    }

    suspend fun buildLineChartData(month: Month): List<Entry> {
        val workEntry = Entry(
            0f,
            TaskRepository.getCompletedTasksCount(month.index, month.year, CATEGORY_WORK).toFloat()
        )
        val personalEntry = Entry(
            1f,
            TaskRepository.getCompletedTasksCount(month.index, month.year, CATEGORY_PERSONAL)
                .toFloat()
        )

        return listOf(workEntry, personalEntry)
    }
}