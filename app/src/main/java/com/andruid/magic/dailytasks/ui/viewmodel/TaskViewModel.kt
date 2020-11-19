package com.andruid.magic.dailytasks.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.andruid.magic.dailytasks.database.TaskRepository

class TaskViewModel : ViewModel() {
    val tasksLiveData = Pager(
        PagingConfig(pageSize = 10),
        pagingSourceFactory = { TaskRepository.getTasks() }
    ).liveData
}