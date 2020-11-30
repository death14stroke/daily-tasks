package com.andruid.magic.dailytasks.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.data.Month
import com.andruid.magic.dailytasks.databinding.ActivityStatisticsBinding
import com.andruid.magic.dailytasks.repository.ChartRepository
import com.andruid.magic.dailytasks.ui.adapter.MonthAdapter
import com.andruid.magic.dailytasks.ui.custom.SliderLayoutManager
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.andruid.magic.dailytasks.ui.viewmodel.MonthViewModel
import com.andruid.magic.dailytasks.util.color
import com.andruid.magic.dailytasks.util.drawable
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlinx.coroutines.launch
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

        initActionBar()
        initMonthSlider()
        initLineChart()
        initBarChart()

        monthViewModel.monthsLiveData.observe(this) {
            monthAdapter.submitData(lifecycle, it)
        }
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        window.statusBarColor = ContextCompat.getColor(this, R.color.scooter)
    }

    private fun initMonthSlider() {
        binding.monthsRv.apply {
            adapter = monthAdapter

            layoutManager = SliderLayoutManager(
                this@StatisticsActivity,
                binding.monthsRv,
                object : SliderLayoutManager.OnItemSelectedListener {
                    override fun onItemSelected(position: Int, view: View?) {
                        val month = monthAdapter.getItemAt(position) ?: return
                        lifecycleScope.launch { buildLineChart(month) }
                        lifecycleScope.launch { buildBarChart(month) }
                    }
                })
        }
    }

    private fun initBarChart() {

    }

    private suspend fun buildBarChart(month: Month) {
        val entryList = ChartRepository.buildBarChartData(month)
        val barDataSet = BarDataSet(entryList, "Daily tasks count")
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
                        else -> ""
                    }
                }
            }
        }
    }

    private suspend fun buildLineChart(month: Month) {
        val entryList = ChartRepository.buildLineChartData(month)
        val lineDataSet = LineDataSet(entryList, "Completed Tasks").apply {
            setDrawFilled(true)

            fillDrawable = drawable(R.drawable.line_chart_bg)
            color = color(R.color.chart_act_line_color)
            setCircleColor(color(R.color.chart_line_color))

            setDrawCircleHole(false)
            lineWidth = 5f
            circleRadius = 10f
        }

        val lineData = LineData(lineDataSet)

        binding.lineChart.apply {
            data = lineData
            invalidate()
        }
    }
}