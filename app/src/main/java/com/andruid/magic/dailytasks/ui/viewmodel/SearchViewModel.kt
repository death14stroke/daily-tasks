package com.andruid.magic.dailytasks.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.andruid.magic.dailytasks.database.TaskRepository

class SearchViewModel : ViewModel() {
    private val queryLiveData = MutableLiveData("")

    val taskSearchResults = queryLiveData.switchMap { query ->
        val pager = Pager(
            PagingConfig(10),
            pagingSourceFactory = { TaskRepository.searchCompletedTasks(query) }
        )
        pager.liveData
    }

    fun updateQuery(query: String) {
        queryLiveData.postValue(query)
    }
}