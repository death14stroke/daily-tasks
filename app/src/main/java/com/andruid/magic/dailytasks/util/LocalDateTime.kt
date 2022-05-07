package com.andruid.magic.dailytasks.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*

fun LocalDateTime.toEpochMillis(): Long {
    return atZone(ZoneId.systemDefault())
        .toInstant().toEpochMilli()
}

fun LocalDate.withFirstDayOfWeek(): LocalDate {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    return with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
}

fun LocalDate.withLastDayOfWeek(): LocalDate {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    return with(TemporalAdjusters.next(firstDayOfWeek))
}