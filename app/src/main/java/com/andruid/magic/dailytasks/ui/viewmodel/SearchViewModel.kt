package com.andruid.magic.dailytasks.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.andruid.magic.dailytasks.database.TaskRepository

class SearchViewModel : ViewModel() {
    private val queryLiveData = MutableLiveData("")

    val taskSearchResults = queryLiveData.switchMap { query ->
        val pager = Pager(
            PagingConfig(10),
            pagingSourceFactory = { TaskRepository.searchCompletedTasks(query) }
        )

        pager.flow
            .cachedIn(viewModelScope)
            .asLiveData()
    }

    fun updateQuery(query: String) {
        queryLiveData.postValue(query)
    }
}