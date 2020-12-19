package com.andruid.magic.dailytasks.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.use
import androidx.lifecycle.lifecycleScope
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.data.Month
import com.andruid.magic.dailytasks.databinding.ActivityStatisticsBinding
import com.andruid.magic.dailytasks.manager.ChartsManager
import com.andruid.magic.dailytasks.ui.adapter.MonthAdapter
import com.andruid.magic.dailytasks.ui.custom.horizontalslider.SliderLayoutManager
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

            smoothScrollToPosition(0)
        }
    }

    private fun initBarChart() {
        val attributes = intArrayOf(android.R.attr.textColorPrimary)
        val dataTextColor = theme.obtainStyledAttributes(attributes).use { typedArray ->
            typedArray.getColorOrThrow(0)
        }

        binding.barChart.apply {
            setScaleEnabled(false)
            description.isEnabled = false
            legend.isEnabled = false

            axisLeft.apply {
                granularity = 1f
                textColor = dataTextColor
            }

            axisRight.setDrawLabels(false)

            xAxis.apply {
                granularity = 1f
                textColor = dataTextColor
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IAxisValueFormatter { value, _ ->
                    "${value.toInt() + 1}"
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    private suspend fun buildBarChart(month: Month) {
        val attributes = intArrayOf(R.attr.colorBarChartBarNormal, R.attr.colorBarChartBarHighlight)
        val (colorNormal, colorHighlight) = binding.barChart.context.theme.obtainStyledAttributes(
            attributes
        ).use { typedArray ->
            typedArray.getColorOrThrow(0) to typedArray.getColorOrThrow(1)
        }

        val entryList = ChartsManager.buildBarChartData(month)
        val barDataSet = BarDataSet(entryList, "").apply {
            color = colorNormal
            highLightColor = colorHighlight
            highLightAlpha = 255
        }
        val barData = BarData(barDataSet).apply {
            setDrawValues(false)
            barWidth = 0.5f
        }

        binding.barChart.apply {
            data = barData
            setVisibleXRange(7f, 7f)
            invalidate()
        }
    }

    private fun initLineChart() {
        val attributes = intArrayOf(android.R.attr.textColorPrimary)
        val dataTextColor = theme.obtainStyledAttributes(attributes).use { typedArray ->
            typedArray.getColorOrThrow(0)
        }

        binding.lineChart.apply {
            setDrawGridBackground(false)
            setScaleEnabled(false)
            description.isEnabled = false
            legend.isEnabled = false

            axisLeft.apply {
                granularity = 1f
                textColor = dataTextColor
            }

            axisRight.setDrawLabels(false)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                textColor = dataTextColor
                setDrawGridLines(false)

                spaceMax = 0.5f
                spaceMin = 0.5f

                valueFormatter = IAxisValueFormatter { value, _ ->
                    when (value) {
                        0f -> "Work"
                        1f -> "Personal"
                        else -> ""
                    }
                }
            }
        }
    }

    private suspend fun buildLineChart(month: Month) {
        val entryList = ChartsManager.buildLineChartData(month)
        val lineDataSet = LineDataSet(entryList, "Completed Tasks").apply {
            setDrawFilled(true)
            setDrawValues(false)

            fillDrawable = drawable(R.drawable.line_chart_bg)
            color = color(R.color.terracotta)
            setCircleColor(color(R.color.sandy_brown))

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