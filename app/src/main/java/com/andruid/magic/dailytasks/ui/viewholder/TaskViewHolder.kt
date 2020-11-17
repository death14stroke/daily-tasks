package com.andruid.magic.dailytasks.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.dailytasks.data.STATUS_DONE
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.databinding.LayoutTaskBinding

class TaskViewHolder private constructor(private val binding: LayoutTaskBinding): RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): TaskViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = LayoutTaskBinding.inflate(inflater, parent, false)
            return TaskViewHolder(binding)
        }
    }

    fun bind(task: Task) {
        binding.task = task
        binding.taskStateIv.isSelected = task.status == STATUS_DONE
    }
}