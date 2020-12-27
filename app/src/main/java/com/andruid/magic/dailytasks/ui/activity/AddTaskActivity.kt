package com.andruid.magic.dailytasks.ui.activity

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.data.CATEGORY_PERSONAL
import com.andruid.magic.dailytasks.data.CATEGORY_WORK
import com.andruid.magic.dailytasks.data.STATUS_PENDING
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.databinding.ActivityAddTaskBinding
import com.andruid.magic.dailytasks.manager.ReminderManager
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.andruid.magic.dailytasks.util.getDateDetails
import kotlinx.coroutines.launch

class AddTaskActivity : ContainerTransformActivity("add_task_transition") {
    private val binding by viewBinding(ActivityAddTaskBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initActionBar()
        initListeners()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.title = null

        binding.toolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initListeners() {
        binding.addTasksBtn.setOnClickListener {
            val title = binding.taskNameET.text.toString().trim()
            if (title.isBlank()) {
                binding.taskNameInput.error = getString(R.string.task_name_error)
                return@setOnClickListener
            }

            val taskMillis = binding.timePickerET.selectedMillis
            val (day, month, year) = getDateDetails(taskMillis)

            val task = Task(
                title = title,
                repeat = binding.repeatSwitch.isChecked,
                startTime = taskMillis,
                day = day,
                month = month,
                year = year,
                category = getCategoryFromRadioGroup(),
                status = STATUS_PENDING
            )

            lifecycleScope.launch {
                val taskId = TaskRepository.insertTask(task)
                ReminderManager.scheduleReminder(this@AddTaskActivity, task.copy(id = taskId))
                onBackPressed()
            }
        }

        binding.taskNameET.addTextChangedListener {
            val name = it.toString().trim()

            binding.taskNameInput.error = if (name.isBlank())
                getString(R.string.username_input_error)
            else
                null
        }
    }

    private fun getCategoryFromRadioGroup(): String {
        return when (binding.categoryRadioGroup.checkedRadioButtonId) {
            R.id.work_radio_btn -> CATEGORY_WORK
            else -> CATEGORY_PERSONAL
        }
    }
}