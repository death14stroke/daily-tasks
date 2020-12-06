package com.andruid.magic.dailytasks.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.data.CATEGORY_PERSONAL
import com.andruid.magic.dailytasks.data.CATEGORY_WORK
import com.andruid.magic.dailytasks.data.STATUS_PENDING
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.databinding.ActivityAddTaskBinding
import com.andruid.magic.dailytasks.repository.ReminderManager
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.andruid.magic.dailytasks.util.getDateDetails
import com.andruid.magic.dailytasks.util.getTaskTimeFromPicker
import kotlinx.coroutines.launch

class AddTaskActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAddTaskBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initListeners()
        initActionBar()
    }

    private fun initActionBar() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = null
            it.setHomeAsUpIndicator(R.drawable.ic_close)
        }
    }

    private fun initListeners() {
        binding.addTasksBtn.setOnClickListener {
            val title = binding.taskNameET.text.toString().trim()
            if (title.isBlank()) {
                binding.taskNameInput.error = "Please enter task name"
                return@setOnClickListener
            }

            val hour = binding.timePickerET.selectedHour
            val minutes = binding.timePickerET.selectedMinute

            val taskMillis = getTaskTimeFromPicker(hour, minutes)
            val (day, month, year) = getDateDetails(taskMillis)
            Log.d("msLog", "selected time = $taskMillis")

            val task = Task(
                title = title,
                repeat = binding.repeatSwitch.isChecked,
                time = taskMillis,
                day = day,
                month = month,
                year = year,
                category = getCategoryFromRadioGroup(),
                status = STATUS_PENDING
            )

            lifecycleScope.launch {
                val taskId = TaskRepository.insertTask(task)
                ReminderManager.scheduleReminder(this@AddTaskActivity, task.copy(id = taskId))
                finish()
            }
        }
    }

    private fun getCategoryFromRadioGroup(): String {
        return when (binding.categoryRadioGroup.checkedRadioButtonId) {
            R.id.work_radio_btn -> CATEGORY_WORK
            else -> CATEGORY_PERSONAL
        }
    }
}