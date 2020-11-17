package com.andruid.magic.dailytasks.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.andruid.magic.dailytasks.database.TaskRepository

class TaskViewModel : ViewModel() {
    private val pager = Pager(
        PagingConfig(pageSize = 10),
        pagingSourceFactory = { TaskRepository.getTasks() }
    )

    val tasksLiveData = pager.flow.cachedIn(viewModelScope).asLiveData()
}