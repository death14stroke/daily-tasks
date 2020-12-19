package com.andruid.magic.dailytasks.ui.viewholder

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.use
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.dailytasks.data.Month
import com.andruid.magic.dailytasks.databinding.LayoutMonthBinding
import com.andruid.magic.dailytasks.ui.custom.horizontalslider.ISliderViewHolder

@SuppressLint("ResourceType")
class MonthViewHolder(private val binding: LayoutMonthBinding) :
    RecyclerView.ViewHolder(binding.root), ISliderViewHolder {
    private val colorAnimation by lazy {
        val attributes =
            intArrayOf(android.R.attr.textColorPrimary, android.R.attr.textColorSecondary)
        val (normalColor, highlightColor) = binding.root.context.obtainStyledAttributes(attributes)
            .use { typedArray ->
                typedArray.getColorOrThrow(0) to typedArray.getColorOrThrow(1)
            }

        ValueAnimator.ofObject(ArgbEvaluator(), normalColor, highlightColor)
    }

    companion object {
        fun from(parent: ViewGroup): MonthViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = LayoutMonthBinding.inflate(inflater, parent, false)

            return MonthViewHolder(binding)
        }
    }

    fun bind(month: Month) {
        binding.month = month

        colorAnimation.addUpdateListener { animator -> binding.monthTv.setTextColor(animator.animatedValue as Int) }
    }

    override fun select() {
        binding.monthTv.isSelected = true
    }

    override fun deselect() {
        binding.monthTv.isSelected = false
    }
}