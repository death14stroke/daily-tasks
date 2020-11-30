package com.andruid.magic.dailytasks.data

import java.util.*

data class Month(
    val index: Int,
    val year: Int,
    val noOfDays: Int,
    val title: String
)

fun Calendar.toMonth(): Month {
    return Month(
        index = get(Calendar.MONTH),
        year = get(Calendar.YEAR),
        noOfDays = getActualMaximum(Calendar.DAY_OF_MONTH),
        title = "${getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())} " +
                "${get(Calendar.YEAR)}"
    )
}