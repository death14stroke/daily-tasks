package com.andruid.magic.dailytasks.repository

import com.andruid.magic.dailytasks.data.CATEGORY_PERSONAL
import com.andruid.magic.dailytasks.data.CATEGORY_WORK
import com.andruid.magic.dailytasks.data.Month
import com.andruid.magic.dailytasks.database.TaskRepository
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChartRepository {
    suspend fun buildBarChartData(month: Month): List<BarEntry> {
        val monthlyStats = TaskRepository.getMonthlyStats(month.index, month.year) ?: emptyList()
        return withContext(Dispatchers.Default) {
            val dbData = mutableMapOf<Int, Int>()
            repeat(month.noOfDays) { day ->
                //TODO: for debug
                dbData[day] = 10
            }

            monthlyStats.forEach { monthStats ->
                dbData[monthStats.day - 1] = monthStats.taskCnt
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