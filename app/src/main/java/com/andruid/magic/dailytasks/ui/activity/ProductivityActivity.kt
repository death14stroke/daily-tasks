package com.andruid.magic.dailytasks.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.use
import androidx.lifecycle.lifecycleScope
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.databinding.ActivityProductivityBinding
import com.andruid.magic.dailytasks.manager.ChartsManager
import com.andruid.magic.dailytasks.ui.adapter.MostActiveTaskAdapter
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.andruid.magic.dailytasks.ui.viewmodel.ProductivityViewModel
import com.andruid.magic.dailytasks.util.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlinx.coroutines.launch
import java.util.*

class ProductivityActivity : ContainerTransformActivity("productivity_transition") {
    private val binding by viewBinding(ActivityProductivityBinding::inflate)
    private val productivityViewModel by viewModels<ProductivityViewModel>()
    private val mostActiveTaskAdapter = MostActiveTaskAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initCollapsingActionBar(binding.toolBar, color(R.color.ebony))
        initStats()
        initHeaders()
        initBarChart()
        initRecyclerView()

        lifecycleScope.launch {
            buildBarChart()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = mostActiveTaskAdapter

        productivityViewModel.mostActiveTasksLiveData.observe(this) {
            mostActiveTaskAdapter.submitList(it)
        }
    }

    private fun initHeaders() {
        val currentWeekStartMillis = getCurrentWeekStartMillis()
        val currentWeekEndMillis = getCurrentWeekEndMillis()

        binding.weekTv.text = getString(
            R.string.week_range,
            showDate(currentWeekStartMillis),
            showDate(currentWeekEndMillis)
        )
        binding.dayTv.text = getCurrentDay()
    }

    private fun initStats() {
        productivityViewModel.tasksThisWeekLiveData.observe(this) {
            binding.weekStats.setCount(it)
        }

        productivityViewModel.tasksThisMonthLiveData.observe(this) {
            binding.monthStats.setCount(it)
        }

        productivityViewModel.totalTasksLiveData.observe(this) {
            binding.totalStats.setCount(it)
        }

        productivityViewModel.timePerTaskLiveData.observe(this) { millis ->
            binding.timePerTaskTv.text = getTimeString(millis)
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

            setDrawBorders(false)

            axisLeft.apply {
                setDrawLabels(false)
                setDrawGridLines(false)
                setDrawAxisLine(false)
            }
            axisRight.apply {
                setDrawLabels(false)
                setDrawGridLines(false)
                setDrawAxisLine(false)
            }

            xAxis.apply {
                granularity = 1f
                position = XAxis.XAxisPosition.BOTTOM
                textColor = dataTextColor

                setDrawGridLines(false)
                setDrawAxisLine(false)

                valueFormatter = IAxisValueFormatter { value, _ ->
                    val cal = Calendar.getInstance().apply {
                        set(Calendar.DAY_OF_WEEK, value.toInt() + 2)
                    }

                    return@IAxisValueFormatter cal.getDisplayName(
                        Calendar.DAY_OF_WEEK,
                        Calendar.SHORT,
                        Locale.getDefault()
                    )
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    private suspend fun buildBarChart() {
        val attributes = intArrayOf(R.attr.colorBarChartBarNormal, R.attr.colorBarChartBarHighlight)
        val (colorNormal, colorHighlight) = binding.barChart.context.theme.obtainStyledAttributes(
            attributes
        ).use { typedArray ->
            typedArray.getColorOrThrow(0) to typedArray.getColorOrThrow(1)
        }

        val entryList =
            ChartsManager.buildBarChartData(getCurrentWeekStartMillis(), getCurrentWeekEndMillis())
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
}