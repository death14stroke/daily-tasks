package com.andruid.magic.dailytasks.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.andruid.magic.dailytasks.databinding.ActivityMainBinding
import com.andruid.magic.dailytasks.ui.adapter.TaskAdapter
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.andruid.magic.dailytasks.ui.viewmodel.TaskViewModel

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val taskAdapter = TaskAdapter()
    private val taskViewModel by viewModels<TaskViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initListeners()
        initRecyclerView()

        //binding.progressView.progress = 45

        taskViewModel.tasksLiveData.observe(this) {
            taskAdapter.submitData(lifecycle, it)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = taskAdapter
    }

    private fun initListeners() {
        binding.addTasksIv.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }
}