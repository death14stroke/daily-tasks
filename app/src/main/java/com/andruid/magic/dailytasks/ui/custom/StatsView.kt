package com.andruid.magic.dailytasks.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.databinding.StatsViewBinding
import kotlin.properties.Delegates

class StatsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: StatsViewBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.stats_view, this, true)
    }

    var title by Delegates.observable("") { _, _, title ->
        binding.title = title
    }
    var count by Delegates.observable(0) { _, _, count ->
        binding.count = count
    }

    init {
        title = ""
        count = 0
    }
}