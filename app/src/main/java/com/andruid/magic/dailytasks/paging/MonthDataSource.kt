package com.andruid.magic.dailytasks.paging

import androidx.paging.PagingSource
import com.andruid.magic.dailytasks.data.Month
import com.andruid.magic.dailytasks.data.toMonth
import com.andruid.magic.dailytasks.util.setMidnight
import java.util.*

class MonthDataSource(private val limitMillis: Long) : PagingSource<Calendar, Month>() {
    private val currCalendar = Calendar.getInstance()
    private val limitCal: Calendar = Calendar.getInstance().apply {
        timeInMillis = limitMillis
        setMidnight()
    }

    override suspend fun load(params: LoadParams<Calendar>): LoadResult<Calendar, Month> {
        val calendar = params.key!!
        val months = mutableListOf<Month>()
        var nextKey: Calendar? = calendar
        var count = params.loadSize

        if (calendar[Calendar.MONTH] == currCalendar[Calendar.MONTH] && calendar[Calendar.YEAR] == currCalendar[Calendar.YEAR]) {
            val month = calendar.toMonth().copy(noOfDays = currCalendar[Calendar.DAY_OF_MONTH])
            months.add(month)
            count--
        }

        while (count-- > 0) {
            if (calendar.timeInMillis < limitCal.timeInMillis && calendar[Calendar.MONTH] != limitCal[Calendar.MONTH]) {
                nextKey = null
                break
            }

            val month = calendar.toMonth()
            months.add(month)

            calendar[Calendar.MONTH]--
        }

        return LoadResult.Page(
            data = months,
            prevKey = null,
            nextKey = nextKey
        )
    }
}