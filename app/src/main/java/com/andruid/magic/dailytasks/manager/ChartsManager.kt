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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

object ChartsManager {
    private val TAG = "${ChartsManager::class.simpleName}Log"

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

        Log.d(TAG, "weekly stats = $weeklyStats")

        return withContext(Dispatchers.Default) {
            val dbData = mutableMapOf<Int, Int>()
            repeat(7) {
                dbData[it] = 0
            }

            val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
            Log.d(TAG, "First day of week = $firstDayOfWeek, value = ${firstDayOfWeek.value}")

            weeklyStats.forEach { weekStats ->
                val localDate = LocalDate.now()
                    .withDayOfMonth(weekStats.day)
                    .withMonth(weekStats.month)
                    .withYear(weekStats.year)
                    .also { Log.d(TAG, "date = $it, day of week = ${it.dayOfWeek}, value = ${it.dayOfWeek.value}") }

                dbData[localDate.mapToDayOfWeek()] = weekStats.taskCnt
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

    private fun LocalDate.mapToDayOfWeek(): Int {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        return if (firstDayOfWeek == DayOfWeek.SUNDAY)
            dayOfWeek.value % firstDayOfWeek.value
        else
            dayOfWeek.value - firstDayOfWeek.value
    }
}
