package com.andruid.magic.dailytasks.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andruid.magic.dailytasks.data.EXTRA_TASK
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.databinding.ActivityAlertBinding
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding

class AlertActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAlertBinding::inflate)
    private val task by lazy {
        intent.extras?.getParcelable<Task>(EXTRA_TASK)
            ?: throw IllegalArgumentException("$EXTRA_TASK is empty")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.task = task

        binding.cancelBtn.setOnClickListener { finish() }
    }
}