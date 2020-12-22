package com.andruid.magic.dailytasks.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.andruid.magic.dailytasks.database.Task

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Task, newItem: Task) =
        oldItem == newItem
}