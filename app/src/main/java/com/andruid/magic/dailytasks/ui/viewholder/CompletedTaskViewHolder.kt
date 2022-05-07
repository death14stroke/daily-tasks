package com.andruid.magic.dailytasks.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.databinding.LayoutCompletedTaskBinding

class CompletedTaskViewHolder(private val binding: LayoutCompletedTaskBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): CompletedTaskViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = LayoutCompletedTaskBinding.inflate(inflater, parent, false)
            return CompletedTaskViewHolder(binding)
        }
    }

    fun bind(task: Task) {
        binding.task = task
    }
}