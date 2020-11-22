package com.andruid.magic.dailytasks.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.ui.viewholder.CompletedTaskViewHolder

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Task, newItem: Task) =
        oldItem == newItem
}

class CompletedTaskAdapter : PagingDataAdapter<Task, CompletedTaskViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CompletedTaskViewHolder.from(parent)

    override fun onBindViewHolder(holder: CompletedTaskViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}