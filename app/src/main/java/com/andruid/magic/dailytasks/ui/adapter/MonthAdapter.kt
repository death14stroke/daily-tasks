package com.andruid.magic.dailytasks.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.andruid.magic.dailytasks.data.Month
import com.andruid.magic.dailytasks.ui.viewholder.MonthViewHolder

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Month>() {
    override fun areContentsTheSame(oldItem: Month, newItem: Month) =
        oldItem == newItem

    override fun areItemsTheSame(oldItem: Month, newItem: Month) =
        oldItem.month == newItem.month && oldItem.year == newItem.year
}

class MonthAdapter : PagingDataAdapter<Month, MonthViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MonthViewHolder.from(parent)

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}