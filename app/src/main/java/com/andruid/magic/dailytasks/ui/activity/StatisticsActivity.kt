package com.andruid.magic.dailytasks.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.data.CATEGORY_PERSONAL
import com.andruid.magic.dailytasks.data.CATEGORY_WORK
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.databinding.ActivityStatisticsBinding
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityStatisticsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initLineChart()

        lifecycleScope.launch {
            buildLineChart()
        }
    }

    private fun initLineChart() {
        binding.lineChart.setDrawGridBackground(false)
        binding.lineChart.xAxis.apply {
            granularity = 1f
            valueFormatter = IAxisValueFormatter { value, _ ->
                when (value) {
                    0f -> "Work"
                    else -> "Personal"
                }
            }
        }
        binding.lineChart.axisLeft.granularity = 1f
    }

    private suspend fun buildLineChart() {
        val lineData = withContext(Dispatchers.Default) {
            val workEntry =
                Entry(0f, TaskRepository.getCompletedTasksCount(CATEGORY_WORK).toFloat())
            val personalEntry =
                Entry(1f, TaskRepository.getCompletedTasksCount(CATEGORY_PERSONAL).toFloat())
            val entryList = listOf(workEntry, personalEntry)

            val lineDataSet = LineDataSet(entryList, "Completed Tasks").apply {
                setDrawFilled(true)
                fillDrawable =
                    ContextCompat.getDrawable(this@StatisticsActivity, R.drawable.line_chart_bg)
                color = ContextCompat.getColor(this@StatisticsActivity, R.color.chart_color)
            }
            LineData(lineDataSet)
        }

        binding.lineChart.apply {
            data = lineData
            invalidate()
        }
    }
}