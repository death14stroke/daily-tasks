package com.andruid.magic.dailytasks.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.databinding.LayoutMostActiveTaskBinding

class MostActiveTaskViewHolder(private val binding: LayoutMostActiveTaskBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): MostActiveTaskViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = LayoutMostActiveTaskBinding.inflate(inflater, parent, false)

            return MostActiveTaskViewHolder(binding)
        }
    }

    fun bind(position: Int, task: Task) {
        binding.position = position
        binding.task = task
    }
}