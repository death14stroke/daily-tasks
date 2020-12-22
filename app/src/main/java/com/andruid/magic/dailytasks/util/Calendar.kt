package com.andruid.magic.dailytasks.util

import java.util.*

fun Calendar.setFirstDayOfWeek(): Calendar {
    set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    setMidnight()

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

fun Calendar.setFirstDayOfMonth(): Calendar {
    set(Calendar.DAY_OF_MONTH, 1)
    setMidnight()

    return this
}

fun Calendar.setMidnight(): Calendar {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)

    return this
}