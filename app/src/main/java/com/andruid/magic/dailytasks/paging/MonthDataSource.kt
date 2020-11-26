package com.andruid.magic.dailytasks.paging

import android.util.Log
import androidx.paging.PagingSource
import com.andruid.magic.dailytasks.data.Month
import java.util.*

class MonthDataSource(private val limitMillis: Long) : PagingSource<Calendar, Month>() {
    private val limitMonth: Int
    private val limitYear: Int

    init {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = limitMillis
        }

        limitMonth = calendar.get(Calendar.MONTH)
        limitYear = calendar.get(Calendar.YEAR)
    }

    override suspend fun load(params: LoadParams<Calendar>): LoadResult<Calendar, Month> {
        var key: Calendar? = params.key ?: return LoadResult.Error(Throwable("Null key"))
        val months = mutableListOf<Month>()

        for (i in 0 until params.loadSize) {
            val month = Month(
                month = key!!.get(Calendar.MONTH),
                year = key.get(Calendar.YEAR),
                title = "${key.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())} ${key.get(Calendar.YEAR)}"
            )

            Log.d("monthLog", "month = $month")

            if ((month.year < limitYear) || (month.year == limitYear && month.month < limitMonth)) {
                key = null
                break
            }

            months.add(month)
            key.add(Calendar.MONTH, -1)
        }

        return LoadResult.Page(
            data = months,
            prevKey = null,
            nextKey = key
        )
    }
}