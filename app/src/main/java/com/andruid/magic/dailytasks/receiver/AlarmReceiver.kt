package com.andruid.magic.dailytasks.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.getSystemService
import com.andruid.magic.dailytasks.data.ACTION_ALARM
import com.andruid.magic.dailytasks.data.EXTRA_TASK_ID
import com.andruid.magic.dailytasks.data.STATUS_DONE
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.manager.ReminderManager
import kotlinx.coroutines.runBlocking

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        private val TAG = "${AlarmReceiver::class.simpleName}Log"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_ALARM) {
            val taskId = intent.extras?.getLong(EXTRA_TASK_ID) ?: return
            val task = runBlocking { TaskRepository.getTaskById(taskId) } ?: return

            Log.d(TAG, "reminder for task ${task.title}")
            if (task.status == STATUS_DONE)
                return

            val notification = ReminderManager.buildNotification(context, task)
            val notificationManager = context.getSystemService<NotificationManager>()!!
            notificationManager.notify((System.currentTimeMillis() / 1000).toInt(), notification)
        }
    }
}