package com.andruid.magic.dailytasks.paging

import android.util.Log
import androidx.paging.PagingSource
import com.andruid.magic.dailytasks.data.Month
import com.andruid.magic.dailytasks.data.toMonth
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
            val month = key!!.toMonth().also {
                Log.d("monthLog", "month = $it")
            }

            if ((month.year < limitYear) || (month.year == limitYear && month.index < limitMonth)) {
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