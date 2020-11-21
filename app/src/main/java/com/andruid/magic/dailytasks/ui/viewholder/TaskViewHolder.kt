package com.andruid.magic.dailytasks.ui.viewholder

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.dailytasks.data.STATUS_DONE
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.databinding.LayoutTaskBinding

class TaskViewHolder private constructor(private val binding: LayoutTaskBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): TaskViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = LayoutTaskBinding.inflate(inflater, parent, false)
            return TaskViewHolder(binding)
        }
    }

    fun bind(task: Task, clickListener: (Task) -> Unit) {
        binding.task = task
        binding.apply {
            val selected = task.status == STATUS_DONE
            taskStateIv.isSelected = selected
            titleTV.isSelected = selected
            timeTV.isSelected = selected
            typeTV.isSelected = selected
        }

        if (task.status == STATUS_DONE)
            binding.titleTV.paintFlags = binding.titleTV.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else
            binding.titleTV.paintFlags =
                binding.titleTV.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

        binding.taskStateIv.setOnClickListener {
            clickListener.invoke(task)
        }
    }
}