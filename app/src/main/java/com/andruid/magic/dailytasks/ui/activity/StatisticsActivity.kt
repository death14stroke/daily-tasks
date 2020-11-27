package com.andruid.magic.dailytasks.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearSnapHelper
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.data.CATEGORY_PERSONAL
import com.andruid.magic.dailytasks.data.CATEGORY_WORK
import com.andruid.magic.dailytasks.data.Month
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.databinding.ActivityStatisticsBinding
import com.andruid.magic.dailytasks.ui.adapter.MonthAdapter
import com.andruid.magic.dailytasks.ui.custom.SliderLayoutManager
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.andruid.magic.dailytasks.ui.viewmodel.MonthViewModel
import com.andruid.magic.dailytasks.util.ScreenUtils
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class StatisticsActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityStatisticsBinding::inflate)
    private val monthViewModel by viewModels<MonthViewModel>()
    private val monthAdapter = MonthAdapter {
        val position = binding.monthsRv.getChildLayoutPosition(it)
        binding.monthsRv.smoothScrollToPosition(position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initMonthSlider()

        monthViewModel.monthsLiveData.observe(this) {
            monthAdapter.submitData(lifecycle, it)
        }

        initLineChart()
        initBarChart()
    }

    private fun initMonthSlider() {
        binding.monthsRv.apply {
            adapter = monthAdapter

            val padding =
                ScreenUtils.getScreenWidth(this@StatisticsActivity) /
                        2 - ScreenUtils.dpToPx(this@StatisticsActivity, 32)
            setPadding(padding, 0, padding, 0)

            layoutManager = SliderLayoutManager(
                this@StatisticsActivity,
                binding.monthsRv,
                object : SliderLayoutManager.OnItemSelectedListener {
                    override fun onItemSelected(position: Int, view: View?) {
                        val month = monthAdapter.getItemAt(position) ?: return
                        Toast.makeText(
                            this@StatisticsActivity,
                            "selected: ${month.title}",
                            Toast.LENGTH_SHORT
                        ).show()
                        lifecycleScope.launch { buildLineChart(month) }
                        lifecycleScope.launch { buildBarChart(month) }
                    }
                })

            LinearSnapHelper().attachToRecyclerView(this)
        }
    }

    private fun initBarChart() {

    }

    private suspend fun buildBarChart(month: Month) {
        val stats = TaskRepository.getMonthlyStats(month.month, month.year)
        val cal = Calendar.getInstance().apply {
            set(Calendar.MONTH, month.month)
            set(Calendar.YEAR, month.year)
        }

        val noOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val dbData = mutableMapOf<Int, Int>()
        repeat(noOfDays) { day ->
            dbData[day] = 0
        }

        stats?.forEach { monthStats ->
            dbData[monthStats.day - 1] = monthStats.taskCnt
        }

        Log.d("dataLog", "$dbData")

        val entries = dbData.map { entry ->
            BarEntry(entry.key.toFloat(), entry.value.toFloat())
        }
        val barDataSet = BarDataSet(entries, "Daily tasks count")
        val barData = BarData(barDataSet)

        binding.barChart.apply {
            data = barData
            invalidate()
        }
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

    private suspend fun buildLineChart(month: Month) {
        val lineData = withContext(Dispatchers.Default) {
            val workEntry =
                Entry(
                    0f,
                    TaskRepository.getCompletedTasksCount(month.month, month.year, CATEGORY_WORK)
                        .toFloat()
                )
            val personalEntry =
                Entry(
                    1f,
                    TaskRepository.getCompletedTasksCount(
                        month.month,
                        month.year,
                        CATEGORY_PERSONAL
                    ).toFloat()
                )
            val entryList = listOf(workEntry, personalEntry)

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