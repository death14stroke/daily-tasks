package com.andruid.magic.dailytasks.ui.activity

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.data.CATEGORY_PERSONAL
import com.andruid.magic.dailytasks.data.CATEGORY_WORK
import com.andruid.magic.dailytasks.data.STATUS_PENDING
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.databinding.ActivityAddTaskBinding
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.andruid.magic.dailytasks.util.getTaskTimeFromPicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAddTaskBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        binding.addTasksBtn.setOnClickListener {
            val task = Task(
                title = binding.taskNameET.text.toString().trim(),
                repeat = binding.repeatSwitch.isChecked,
                time = System.currentTimeMillis(),
                category = getCategoryFromRadioGroup(),
                status = STATUS_PENDING
            )

            lifecycleScope.launch {
                TaskRepository.insertTask(task)
                finish()
            }
        }

        binding.timePickerET.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val hour: Int = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute: Int = currentTime.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val ms = getTaskTimeFromPicker(selectedHour, selectedMinute)
                val dateFormat = SimpleDateFormat(" hh:mm a", Locale.getDefault())
                binding.timePickerET.setText(dateFormat.format(ms))
            }, hour, minute, false).apply {
                title = "Select time"
                show()
            }
        }
    }

    private fun getCategoryFromRadioGroup(): String {
        return when (binding.categortRadioGroup.checkedRadioButtonId) {
            R.id.work_radio_btn -> CATEGORY_WORK
            else -> CATEGORY_PERSONAL
        }
    }
}