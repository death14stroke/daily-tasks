package com.andruid.magic.dailytasks.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.paging.MonthDataSource
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class MonthViewModel : ViewModel() {
    val monthsLiveData = Pager(
        PagingConfig(pageSize = 10),
        initialKey = LocalDate.now(),
        pagingSourceFactory = {
            val limitMillis = runBlocking { TaskRepository.getTaskHistoryStartTime() }
            MonthDataSource(limitMillis ?: System.currentTimeMillis())
        }
    ).flow.cachedIn(viewModelScope).asLiveData()
}