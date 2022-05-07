package com.andruid.magic.dailytasks.data

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

data class Month(
    val index: Int,
    val year: Int,
    val noOfDays: Int,
    val title: String
)

fun LocalDate.toMonth(): Month {
    return Month(
        index = monthValue,
        year = year,
        noOfDays = month.length(isLeapYear),
        title = "${month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} $year"
    )
}