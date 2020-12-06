package com.andruid.magic.dailytasks.ui.custom

import android.content.Context
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class TimePickerEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputEditText(context, attrs, defStyleAttr) {
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
        updateUI(currHours, currMinutes)

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
        val (currHour, currMinute) = getCurrentHourMinutes()

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(currHour)
            .setMinute(currMinute)
            .setTitleText("Select time")
            .build()
        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute

            updateUI(hour, minute)
        }

        getSupportFragmentManager(context)?.let { fragmentManager ->
            timePicker.show(fragmentManager, "time-picker-dialog")
        }
    }

    private fun updateUI(hour: Int, minute: Int) {
        val ms = getMilliSecondsForTime(hour, minute)
        setText(formatTime(ms))

        selectedHour = hour
        selectedMinute = minute

        onTimeChangeListener?.invoke(hour, minute)
    }

    private fun View.getSupportFragmentManager(context: Context): FragmentManager? {
        return when (context) {
            is AppCompatActivity -> context.supportFragmentManager
            is ContextThemeWrapper -> getSupportFragmentManager(context.baseContext)
            else -> null
        }
    }
}