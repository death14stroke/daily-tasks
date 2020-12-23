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

            val calendar = Calendar.getInstance().apply {
                firstDayOfWeek = Calendar.MONDAY
            }

            weeklyStats.forEach { weekStats ->
                calendar[Calendar.DAY_OF_MONTH] = weekStats.day
                calendar[Calendar.MONTH] = weekStats.month
                calendar[Calendar.YEAR] = weekStats.year

                Log.d("calLog", "${weekStats.day}/${weekStats.month}/${weekStats.year} - day of week = ${calendar[Calendar.DAY_OF_WEEK]}")

                dbData[calendar[Calendar.DAY_OF_WEEK] - calendar.firstDayOfWeek] = weekStats.taskCnt
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