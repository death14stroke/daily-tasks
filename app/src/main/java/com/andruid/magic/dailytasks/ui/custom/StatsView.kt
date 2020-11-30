package com.andruid.magic.dailytasks.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.getDrawableOrThrow
import androidx.core.content.res.getStringOrThrow
import androidx.core.content.res.use
import androidx.databinding.DataBindingUtil
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.databinding.StatsViewBinding

class StatsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: StatsViewBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding =
            DataBindingUtil.inflate<StatsViewBinding>(inflater, R.layout.stats_view, this, true)
                .apply {
                    count = 0
                }

        context.obtainStyledAttributes(attrs, R.styleable.StatsView, defStyleAttr, 0).use {
            val drawable = it.getDrawableOrThrow(R.styleable.StatsView_background)
            binding.rootLayout.background = drawable

            val title = it.getStringOrThrow(R.styleable.StatsView_title)
            binding.titleTv.text = title
        }
    }

    fun setCount(count: Int) {
        binding.count = count
    }
}