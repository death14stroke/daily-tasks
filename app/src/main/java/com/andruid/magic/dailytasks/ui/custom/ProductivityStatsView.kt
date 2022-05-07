package com.andruid.magic.dailytasks.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.res.getStringOrThrow
import androidx.core.content.res.use
import androidx.databinding.DataBindingUtil
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.databinding.ProductivityStatsViewBinding
import com.google.android.material.circularreveal.CircularRevealLinearLayout

class ProductivityStatsView(context: Context, attrs: AttributeSet?) :
    CircularRevealLinearLayout(context, attrs) {
    private val binding: ProductivityStatsViewBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.productivity_stats_view, this, true)

        context.obtainStyledAttributes(attrs, R.styleable.ProductivityStatsView, 0, 0).use {
            val title = it.getStringOrThrow(R.styleable.ProductivityStatsView_title)
            binding.titleTv.text = title
        }
    }

    fun setCount(count: Int) {
        binding.count = count
    }
}