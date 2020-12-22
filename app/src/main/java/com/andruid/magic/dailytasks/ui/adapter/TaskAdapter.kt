package com.andruid.magic.dailytasks.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.ui.viewholder.TaskViewHolder

class TaskAdapter(private val clickListener: (Task) -> Unit) :
    PagingDataAdapter<Task, TaskViewHolder>(TaskDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TaskViewHolder.from(parent)

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, clickListener)
        }
    }
}