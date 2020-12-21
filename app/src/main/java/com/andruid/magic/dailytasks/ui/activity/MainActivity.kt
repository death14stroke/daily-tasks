package com.andruid.magic.dailytasks.ui.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.andruid.magic.dailytasks.data.STATUS_DONE
import com.andruid.magic.dailytasks.data.STATUS_PENDING
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.databinding.ActivityMainBinding
import com.andruid.magic.dailytasks.repository.ProfileRepository
import com.andruid.magic.dailytasks.ui.adapter.TaskAdapter
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.andruid.magic.dailytasks.ui.viewmodel.TaskViewModel
import com.andruid.magic.dailytasks.util.getGreetingMessage
import com.andruid.magic.dailytasks.util.showCompleteTaskDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val taskViewModel by viewModels<TaskViewModel>()
    private val taskClickListener = { task: Task ->
        if (task.status == STATUS_PENDING) {
            lifecycleScope.launch {
                val completed = showCompleteTaskDialog()
                if (completed)
                    TaskRepository.completeTask(task.id, System.currentTimeMillis())
            }
        }
    }
    private val taskAdapter by lazy { TaskAdapter(taskClickListener) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initListeners()
        initRecyclerView()
        initStats()
        initGreeting()

        taskViewModel.tasksLiveData.observe(this) {
            taskAdapter.submitData(lifecycle, it)
        }

        taskViewModel.progressLiveData.observe(this) {
            binding.progressView.progress = it
        }
    }

    private fun initGreeting() {
        val greeting = getGreetingMessage()
        lifecycleScope.launch {
            ProfileRepository.getUser().collect { user ->
                val message = "$greeting\n${user!!.userName}"
                val span = SpannableString(message).apply {
                    val start = greeting.length + 1
                    setSpan(
                        StyleSpan(Typeface.BOLD),
                        start,
                        message.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                binding.greetingsTv.setText(span, TextView.BufferType.SPANNABLE)
                binding.profileIv.load(File(user.profileImagePath))
            }
        }
    }

    private fun initStats() {
        taskViewModel.tasksCompletedLiveData.observe(this) {
            binding.completedTasksBtn.setCount(it)
        }

        taskViewModel.tasksPerDayLiveData.observe(this) {
            binding.tasksRateBtn.setCount(it)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = taskAdapter
    }

    private fun initListeners() {
        binding.addTasksIv.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        binding.completedTasksBtn.setOnClickListener {
            startActivity(Intent(this, CompletedTasksActivity::class.java))
        }

        binding.tasksRateBtn.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }
    }
}