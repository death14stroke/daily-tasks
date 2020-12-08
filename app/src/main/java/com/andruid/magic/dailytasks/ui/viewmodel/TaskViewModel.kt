package com.andruid.magic.dailytasks.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.andruid.magic.dailytasks.data.STATUS_DONE
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.util.StatsCalculator

class TaskViewModel : ViewModel() {
    val tasksLiveData = Pager(
        PagingConfig(pageSize = 10),
        pagingSourceFactory = { TaskRepository.getTasks() }
    ).flow.cachedIn(viewModelScope).asLiveData()

    val progressLiveData = StatsCalculator.calculateDailyProgress()
        .asLiveData()

    val tasksCompletedLiveData = TaskRepository.getStatusTasksCount(STATUS_DONE).asLiveData()

    val tasksPerDayLiveData = StatsCalculator.calculateTasksPerDay()
        .asLiveData()
}