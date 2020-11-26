package com.andruid.magic.dailytasks.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.databinding.ActivityStatisticsBinding
import com.andruid.magic.dailytasks.ui.adapter.MonthAdapter
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.andruid.magic.dailytasks.ui.viewmodel.MonthViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class StatisticsActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityStatisticsBinding::inflate)
    private val monthViewModel by viewModels<MonthViewModel>()
    private val monthAdapter = MonthAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.monthsRv.adapter = monthAdapter
        monthViewModel.monthsLiveData.observe(this) {
            monthAdapter.submitData(lifecycle, it)
        }

        initLineChart()

        lifecycleScope.launch { buildLineChart() }
        lifecycleScope.launch { buildBarChart() }
    }

    private suspend fun buildBarChart() {
        val (month, year) = Calendar.getInstance().run {
            get(Calendar.MONTH) to get(Calendar.YEAR)
        }

        val data = TaskRepository.getMonthlyStats(month, year)
        val startTime = TaskRepository.getTaskHistoryStartTime()
        val (startMonth, startYear) = Calendar.getInstance().run {
            timeInMillis = startTime

            return@run get(Calendar.MONTH) to get(Calendar.YEAR)
        }

        Log.d("dataLog", "$data - start time = $startTime")
    }

    private fun initLineChart() {
        binding.lineChart.apply {
            setDrawGridBackground(false)
            setScaleEnabled(false)
            description.isEnabled = false

            legend.isEnabled = false
            axisLeft.granularity = 1f
            axisRight.setDrawLabels(false)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                valueFormatter = IAxisValueFormatter { value, _ ->
                    when (value) {
                        1f -> "Work"
                        2f -> "Personal"
                        3f -> "Hobby"
                        else -> ""
                    }
                }
            }
        }
    }

    private suspend fun buildLineChart() {
        val lineData = withContext(Dispatchers.Default) {
            //TODO: commented for actual use
            /*val workEntry =
                Entry(0f, TaskRepository.getCompletedTasksCount(CATEGORY_WORK).toFloat())
            val personalEntry =
                Entry(1f, TaskRepository.getCompletedTasksCount(CATEGORY_PERSONAL).toFloat())
            val entryList = listOf(workEntry, personalEntry)*/

            val originEntry = Entry(0f, 0f)
            val workEntry = Entry(1f, 101f)
            val personalEntry = Entry(2f, 148f)
            val hobbyEntry = Entry(3f, 53f)
            val endEntry = Entry(4f, 0f)
            val entryList = listOf(originEntry, workEntry, personalEntry, hobbyEntry, endEntry)

            val lineDataSet = LineDataSet(entryList, "Completed Tasks").apply {
                setDrawFilled(true)
                fillDrawable =
                    ContextCompat.getDrawable(this@StatisticsActivity, R.drawable.line_chart_bg)
                color =
                    ContextCompat.getColor(this@StatisticsActivity, R.color.chart_act_line_color)
                setCircleColor(
                    ContextCompat.getColor(
                        this@StatisticsActivity,
                        R.color.chart_line_color
                    )
                )
                setDrawCircleHole(false)
                lineWidth = 5f
                circleRadius = 10f

            }

            LineData(lineDataSet)
        }

        binding.lineChart.apply {
            data = lineData
            invalidate()
        }
    }
}