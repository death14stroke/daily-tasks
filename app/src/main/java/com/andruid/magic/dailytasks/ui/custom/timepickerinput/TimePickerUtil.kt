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