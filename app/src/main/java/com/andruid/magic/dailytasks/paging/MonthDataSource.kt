package com.andruid.magic.dailytasks.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andruid.magic.dailytasks.data.Month
import com.andruid.magic.dailytasks.data.toMonth
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class MonthDataSource(limitMillis: Long) : PagingSource<LocalDate, Month>() {
    private val currDate = LocalDate.now()
    private val limitDate =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(limitMillis), ZoneId.systemDefault())
            .withDayOfMonth(1)
            .withHour(0)
            .truncatedTo(ChronoUnit.HOURS)
            .toLocalDate()

    override suspend fun load(params: LoadParams<LocalDate>): LoadResult<LocalDate, Month> {
        var localDate = params.key!!
        val months = mutableListOf<Month>()
        var nextKey: LocalDate? = localDate
        var count = params.loadSize

        if (localDate.year == currDate.year && localDate.month == currDate.month) {
            val month = localDate.toMonth().copy(noOfDays = currDate.dayOfMonth)
            months.add(month)
            count--

            localDate = localDate.minusMonths(1)
        }

        while (count-- > 0) {
            if (localDate.isBefore(limitDate)) {
                nextKey = null
                break
            }

            val month = localDate.toMonth()
            months.add(month)

            localDate = localDate.minusMonths(1)
        }

        if (nextKey != null)
            nextKey = localDate

        return LoadResult.Page(
            data = months,
            prevKey = null,
            nextKey = nextKey
        )
    }

    override fun getRefreshKey(state: PagingState<LocalDate, Month>): LocalDate? = null
}