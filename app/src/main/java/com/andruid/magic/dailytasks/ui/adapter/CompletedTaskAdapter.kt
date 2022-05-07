package com.andruid.magic.dailytasks.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.ui.viewholder.CompletedTaskViewHolder

class CompletedTaskAdapter : PagingDataAdapter<Task, CompletedTaskViewHolder>(TaskDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CompletedTaskViewHolder.from(parent)

    override fun onBindViewHolder(holder: CompletedTaskViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}