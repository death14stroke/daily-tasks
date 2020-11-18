package com.andruid.magic.dailytasks.util

import com.andruid.magic.dailytasks.ui.custom.getMilliSecondsForTime

fun getTaskTimeFromPicker(hour: Int, minutes: Int): Long {
    val time = getMilliSecondsForTime(hour, minutes)
    val currentTime = System.currentTimeMillis()

    if (time - currentTime >= 60 * 1000L)
        return time
    return getMilliSecondsForTime(hour, minutes) + 24 * 60 * 60 * 1000L
}