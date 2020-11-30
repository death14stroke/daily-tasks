package com.andruid.magic.dailytasks.ui.viewholder

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.data.Month
import com.andruid.magic.dailytasks.databinding.LayoutMonthBinding
import com.andruid.magic.dailytasks.ui.custom.ISliderViewHolder
import com.andruid.magic.dailytasks.util.color


class MonthViewHolder(private val binding: LayoutMonthBinding) :
    RecyclerView.ViewHolder(binding.root), ISliderViewHolder {
    private lateinit var colorAnimation: ValueAnimator

    companion object {
        fun from(parent: ViewGroup): MonthViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = LayoutMonthBinding.inflate(inflater, parent, false)

            return MonthViewHolder(binding)
        }
    }

    fun bind(month: Month) {
        binding.month = month

        val context = binding.root.context
        val normalColor = context.color(R.color.bright_gray)
        val highLightColor = context.color(R.color.white)

        colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), normalColor, highLightColor)
        colorAnimation.addUpdateListener { animator -> binding.monthTv.setTextColor(animator.animatedValue as Int) }
    }

    override fun select() {
        val context = binding.root.context
        binding.monthTv.setTextColor(context.color(R.color.white))
    }

    override fun deselect() {
        val context = binding.root.context
        binding.monthTv.setTextColor(context.color(R.color.bright_gray))
    }
}