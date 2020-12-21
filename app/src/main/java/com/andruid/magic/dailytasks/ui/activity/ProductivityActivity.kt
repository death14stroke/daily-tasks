package com.andruid.magic.dailytasks.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.andruid.magic.dailytasks.databinding.ActivityProductivityBinding
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.andruid.magic.dailytasks.ui.viewmodel.ProductivityViewModel

class ProductivityActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityProductivityBinding::inflate)
    private val productivityViewModel by viewModels<ProductivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initActionBar()
        initStats()
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
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}