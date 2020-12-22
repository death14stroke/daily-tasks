package com.andruid.magic.dailytasks.util

import com.andruid.magic.dailytasks.ui.custom.timepickerinput.getMilliSecondsForTime
import java.text.SimpleDateFormat
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
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return calendar.timeInMillis
}

fun getDateDetails(millis: Long): Array<Int> {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = millis
    }

    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    return arrayOf(day, month, year)
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

fun getCurrentWeekStartMillis(): Long {
    val calendar = Calendar.getInstance().setFirstDayOfWeek()

    return calendar.timeInMillis
}

fun getCurrentWeekEndMillis(): Long {
    val calendar = Calendar.getInstance().setLastDayOfWeek()

    return calendar.timeInMillis
}

fun getCurrentMonthMillis(): Long {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return calendar.timeInMillis
}

fun showDate(millis: Long): String {
    val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
    return sdf.format(millis)
}

fun getCurrentDay(): String {
    return Calendar.getInstance()
        .getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) ?: "Undefined"
}

fun Calendar.setFirstDayOfWeek(): Calendar {
    set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)

    return this
}

fun Calendar.setLastDayOfWeek(): Calendar {
    set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
    set(Calendar.HOUR_OF_DAY, 23)
    set(Calendar.MINUTE, 59)
    set(Calendar.SECOND, 59)
    set(Calendar.MILLISECOND, 0)

    return this
}