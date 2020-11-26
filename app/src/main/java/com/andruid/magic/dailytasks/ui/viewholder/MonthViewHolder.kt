package com.andruid.magic.dailytasks.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.dailytasks.data.Month
import com.andruid.magic.dailytasks.databinding.LayoutMonthBinding

class MonthViewHolder(private val binding: LayoutMonthBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): MonthViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = LayoutMonthBinding.inflate(inflater, parent, false)

            return MonthViewHolder(binding)
        }
    }

    fun bind(month: Month) {
        binding.month = month
    }
}