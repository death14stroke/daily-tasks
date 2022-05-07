package com.andruid.magic.dailytasks.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.manager.StatisticsManager
import com.andruid.magic.dailytasks.util.getCurrentMonthMillis
import com.andruid.magic.dailytasks.util.getCurrentWeekStartMillis

class ProductivityViewModel : ViewModel() {
    val timePerTaskLiveData = StatisticsManager.calculateTimePerTask()
        .asLiveData()

    val tasksThisWeekLiveData =
        TaskRepository.getTotalTasksCount(getCurrentWeekStartMillis(), System.currentTimeMillis())
            .asLiveData()

    val tasksThisMonthLiveData =
        TaskRepository.getTotalTasksCount(getCurrentMonthMillis(), System.currentTimeMillis())
            .asLiveData()

    val totalTasksLiveData =
        TaskRepository.getTotalTasksCount()
            .asLiveData()

    val mostActiveTasksLiveData =
        TaskRepository.getMostActiveTasks()
            .asLiveData()
}