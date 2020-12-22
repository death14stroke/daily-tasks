package com.andruid.magic.dailytasks.util

import com.andruid.magic.dailytasks.ui.custom.timepickerinput.getMilliSecondsForTime
import java.util.*

fun getTaskTimeFromPicker(hour: Int, minutes: Int): Long {
    val time = getMilliSecondsForTime(hour, minutes)
    val currentTime = System.currentTimeMillis()

    if (time - currentTime >= 60 * 1000L)
        return time
    return getMilliSecondsForTime(hour, minutes) + 24 * 60 * 60 * 1000L
}

fun getMidnightTimeMillis(dayOffset: Int = 0): Long {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + dayOffset)
        setMidnight()
    }

    return calendar.timeInMillis
}

fun getGreetingMessage(): String {
    val c = Calendar.getInstance()

    return when (c.get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good Morning"
        in 12..15 -> "Good Afternoon"
        in 16..20 -> "Good Evening"
        in 21..23 -> "Good Night"
        else -> "Hello"
    }
}

fun getCurrentWeekStartMillis() =
    Calendar.getInstance().setFirstDayOfWeek().timeInMillis

fun getCurrentWeekEndMillis() =
    Calendar.getInstance().setLastDayOfWeek().timeInMillis

fun getCurrentMonthMillis() =
    Calendar.getInstance().setFirstDayOfMonth().timeInMillis

fun getCurrentDay() =
    Calendar.getInstance()
        .getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) ?: "Undefined"