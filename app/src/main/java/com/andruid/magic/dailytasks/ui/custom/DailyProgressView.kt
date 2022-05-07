package com.andruid.magic.dailytasks.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.databinding.DailyProgressViewBinding
import kotlin.properties.Delegates

class DailyProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: DailyProgressViewBinding
    var progress by Delegates.observable(0) { _, _, prog ->
        updateProgress(prog)
    }

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.daily_progress_view, this, true)
        progress = 0
    }

    private fun updateProgress(progress: Int) {
        binding.progress = progress
    }
}