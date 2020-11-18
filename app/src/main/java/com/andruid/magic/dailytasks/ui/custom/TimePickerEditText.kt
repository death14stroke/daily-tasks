package com.andruid.magic.dailytasks.ui.custom

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import com.google.android.material.textfield.TextInputEditText

class TimePickerEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputEditText(context, attrs, defStyleAttr) {
    private val timeSelectListener =
        TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            val ms = getMilliSecondsForTime(hour, minute)
            setText(formatTime(ms))

            selectedHour = hour
            selectedMinute = minute

            onTimeChangeListener?.invoke(hour, minute)
        }

    var onTimeChangeListener: ((Int, Int) -> Unit)? = null

    var selectedHour = 0
        private set
    var selectedMinute = 0
        private set

    init {
        inputType = InputType.TYPE_NULL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            isFocusable = false

        val (currHours, currMinutes) = getCurrentHourMinutes()
        timeSelectListener.onTimeSet(null, currHours, currMinutes)
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

        TimePickerDialog(context, timeSelectListener, hour, minute, false).apply {
            setTitle("Select time")
            show()
        }
    }
}