package com.andruid.magic.dailytasks.ui.custom

import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.andruid.magic.dailytasks.util.getTaskTimeFromPicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class TimePickerEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputEditText(context, attrs, defStyleAttr) {
    private var onTimeChangeListener: ((Int, Int) -> Unit)? = null

    init {
        setOnClickListener(null)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        val listener = { view: View ->
            l?.onClick(view)
            showTimePicker()
        }

        super.setOnClickListener(listener)
    }

    private fun showTimePicker() {
        val (hour, minute) = getCurrentHourMinutes()

        TimePickerDialog(context, { _, selectedHour, selectedMinute ->
            val ms = getTaskTimeFromPicker(selectedHour, selectedMinute)
            val dateFormat = SimpleDateFormat(" hh:mm a", Locale.getDefault())
            setText(dateFormat.format(ms))
            onTimeChangeListener?.invoke(selectedHour, selectedMinute)
        }, hour, minute, false).apply {
            setTitle("Select time")
            show()
        }
    }

    private fun getCurrentHourMinutes(): Pair<Int, Int> {
        return Calendar.getInstance().run {
            val hour = get(Calendar.HOUR_OF_DAY)
            val minute = get(Calendar.MINUTE)

            hour to minute
        }
    }
}