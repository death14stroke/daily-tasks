package com.andruid.magic.dailytasks.ui.custom.timepickerinput

import android.content.Context
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.FragmentManager
import com.andruid.magic.dailytasks.util.getTaskTimeFromPicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class TimePickerEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputEditText(context, attrs, defStyleAttr) {
    var selectedMillis = 0L
        private set

    init {
        inputType = InputType.TYPE_NULL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            isFocusable = false

        updateUI(System.currentTimeMillis())

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
            val millis = getTaskTimeFromPicker(hour, minute)

            updateUI(millis)
        }

        getSupportFragmentManager(context)?.let { fragmentManager ->
            timePicker.show(fragmentManager, "time-picker-dialog")
        }
    }

    private fun updateUI(millis: Long) {
        val localDateTime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
        setText(getTimeString(millis, localDateTime.dayOfMonth))

        selectedMillis = millis
    }

    private fun View.getSupportFragmentManager(context: Context): FragmentManager? {
        return when (context) {
            is AppCompatActivity -> context.supportFragmentManager
            is ContextThemeWrapper -> getSupportFragmentManager(context.baseContext)
            else -> null
        }
    }

    private fun getTimeString(millis: Long, day: Int): String {
        val today = LocalDate.now().dayOfMonth
        val dayString = if (today == day)
            "Today"
        else
            "Tomorrow"

        return "${formatTime(millis)}, $dayString"
    }
}