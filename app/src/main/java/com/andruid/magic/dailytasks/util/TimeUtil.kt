package com.andruid.magic.dailytasks.util

import java.util.*

fun getMilliSecondsFromTime(hour: Int, minutes: Int, tomorrow: Boolean = false): Long {
    val calender = Calendar.getInstance().apply {
        if (tomorrow)
            set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minutes)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return calender.timeInMillis
}

fun getTaskTimeFromPicker(hour: Int, minutes: Int): Long {
    val time = getMilliSecondsFromTime(hour, minutes)
    val currentTime = System.currentTimeMillis()

    if (time - currentTime >= 60 * 1000L)
        return time
    return getMilliSecondsFromTime(hour, minutes, true)
}