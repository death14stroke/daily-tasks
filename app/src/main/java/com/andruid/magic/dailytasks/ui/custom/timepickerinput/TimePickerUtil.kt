package com.andruid.magic.dailytasks.ui.custom.timepickerinput

import java.text.SimpleDateFormat
import java.util.*

fun formatTime(ms: Long): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return dateFormat.format(ms)
}

internal fun getCurrentHourMinutes(): Pair<Int, Int> {
    return Calendar.getInstance().run {
        val hour = get(Calendar.HOUR_OF_DAY)
        val minute = get(Calendar.MINUTE)

        hour to minute
    }
}

fun getMilliSecondsForTime(hour: Int, minutes: Int): Long {
    val calender = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minutes)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return calender.timeInMillis
}