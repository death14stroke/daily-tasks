package com.andruid.magic.dailytasks.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.andruid.magic.dailytasks.data.STATUS_DONE
import com.andruid.magic.dailytasks.data.STATUS_PENDING
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.databinding.ActivityMainBinding
import com.andruid.magic.dailytasks.ui.adapter.TaskAdapter
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.andruid.magic.dailytasks.ui.viewmodel.TaskViewModel
import com.andruid.magic.dailytasks.util.showCompleteTaskDialog
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val taskViewModel by viewModels<TaskViewModel>()
    private val taskClickListener = { task: Task ->
        if (task.status == STATUS_PENDING) {
            lifecycleScope.launch {
                val completed = showCompleteTaskDialog()
                if (completed)
                    TaskRepository.updateTaskStatus(task.id, STATUS_DONE)
            }
        }
    }
    private val taskAdapter by lazy { TaskAdapter(taskClickListener) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initListeners()
        initRecyclerView()

        taskViewModel.tasksLiveData.observe(this) {
            taskAdapter.submitData(lifecycle, it)
        }

        taskViewModel.progressLiveData.observe(this) {
            binding.progressView.progress = it
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