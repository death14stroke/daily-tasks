package com.andruid.magic.dailytasks.manager

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.data.ACTION_ALARM
import com.andruid.magic.dailytasks.data.EXTRA_TASK
import com.andruid.magic.dailytasks.data.EXTRA_TASK_ID
import com.andruid.magic.dailytasks.database.Task
import com.andruid.magic.dailytasks.receiver.AlarmReceiver
import com.andruid.magic.dailytasks.ui.activity.AlertActivity
import com.andruid.magic.dailytasks.ui.activity.MainActivity
import java.util.*

object ReminderManager {
    private const val REMINDER_CHANNEL_NAME = "Tasks reminder"
    private const val REMINDER_CHANNEL_ID = "tasks_reminder_channel"

    fun scheduleReminder(context: Context, task: Task) {
        val intent = Intent(context, AlarmReceiver::class.java)
            .setAction(ACTION_ALARM)
            .putExtra(EXTRA_TASK_ID, task.id)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context.getSystemService<AlarmManager>()!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, task.startTime, pendingIntent
            )
        else
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.startTime, pendingIntent)
    }

    fun buildNotification(context: Context, task: Task): Notification {
        val notificationManager = context.getSystemService<NotificationManager>()!!

        var importance = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            importance = NotificationManager.IMPORTANCE_HIGH

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                REMINDER_CHANNEL_ID, REMINDER_CHANNEL_NAME, importance
            ).apply {
                enableLights(true)
                lightColor = Color.GREEN
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val fullScreenIntent = Intent(context, AlertActivity::class.java)
            .putExtra(EXTRA_TASK, task)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            0,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setContentTitle(context.getString(R.string.reminder_noti_title))
            .setContentText(task.title)
            .setSubText(task.category.toUpperCase(Locale.getDefault()))
            .setShowWhen(true)
            .build()
    }
}