package com.andruid.magic.dailytasks.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.ui.viewholder.MostActiveTaskViewHolder

class MostActiveTaskAdapter : ListAdapter<Task, MostActiveTaskViewHolder>(TaskDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MostActiveTaskViewHolder.from(parent)

    override fun onBindViewHolder(holder: MostActiveTaskViewHolder, position: Int) {
        getItem(position)?.let { task ->
            holder.bind(position + 1, task)
        }
    }
}