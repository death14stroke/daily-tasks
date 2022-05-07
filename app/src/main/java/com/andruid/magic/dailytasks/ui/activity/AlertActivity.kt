package com.andruid.magic.dailytasks.ui.activity

import android.app.KeyguardManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
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

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            getSystemService<KeyguardManager>()?.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        binding.task = task

        binding.cancelBtn.setOnClickListener { finish() }
    }
}