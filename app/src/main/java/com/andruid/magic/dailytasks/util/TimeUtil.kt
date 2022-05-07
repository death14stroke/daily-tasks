package com.andruid.magic.dailytasks.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*

private const val TASK_WAIT_TIME_MILLIS = 2 * 60 * 1000L

fun getTaskTimeFromPicker(hour: Int, minutes: Int): Long {
    val dateTime = LocalDateTime.now()
        .withHour(hour)
        .withMinute(minutes)

    val time = dateTime.toEpochMillis()
    val currentTime = Instant.now().toEpochMilli()

    if (time - currentTime >= TASK_WAIT_TIME_MILLIS)
        return time

    return dateTime
        .plusDays(1)
        .toEpochMillis()
}

fun getMidnightTimeMillis(dayOffset: Int = 0): Long {
    return LocalDate.now()
        .plusDays(dayOffset.toLong())
        .atStartOfDay()
        .toEpochMillis()
}

fun getGreetingMessage(): String {
    val hour = LocalTime.now()
        .hour

    return when (hour) {
        in 0..11 -> "Good Morning"
        in 12..15 -> "Good Afternoon"
        in 16..20 -> "Good Evening"
        in 21..23 -> "Good Night"
        else -> "Hello"
    }
}

fun getCurrentWeekStartMillis(): Long {
    return LocalDate.now()
        .withFirstDayOfWeek()
        .atStartOfDay()
        .toEpochMillis()
}

fun getCurrentWeekEndMillis(): Long {
    return LocalDate.now()
        .withLastDayOfWeek()
        .atStartOfDay()
        .toEpochMillis()
}

fun getCurrentMonthMillis(): Long {
    return LocalDate.now()
        .withDayOfMonth(1)
        .atStartOfDay()
        .toEpochMillis()
}

fun getCurrentDay(): String {
    return LocalDate.now()
        .dayOfWeek
        .getDisplayName(TextStyle.FULL, Locale.getDefault())
}