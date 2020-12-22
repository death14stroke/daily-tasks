package com.andruid.magic.dailytasks.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun getTimeString(millis: Long): String {
    val days = TimeUnit.MILLISECONDS.toDays(millis)
    if (days > 0)
        return "$days days"

    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    if (hours > 0)
        return "$hours h"

    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    return "$minutes min"
}

fun showDate(millis: Long): String {
    val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
    return sdf.format(millis)
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