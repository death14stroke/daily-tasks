package com.andruid.magic.dailytasks.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.paging.MonthDataSource
import kotlinx.coroutines.runBlocking
import java.util.*

class MonthViewModel : ViewModel() {
    val monthsLiveData = Pager(
        PagingConfig(pageSize = 10),
        initialKey = Calendar.getInstance(),
        pagingSourceFactory = {
            val limitMillis = runBlocking { TaskRepository.getTaskHistoryStartTime() }
            MonthDataSource(limitMillis ?: System.currentTimeMillis())
        }
    ).liveData
}