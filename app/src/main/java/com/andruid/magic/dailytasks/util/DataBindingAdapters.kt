package com.andruid.magic.dailytasks.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.andruid.magic.dailytasks.ui.custom.timepickerinput.formatTime
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("showTime")
fun TextView.showTimeFromMillis(millis: Long) {
    val timeString = formatTime(millis)
    text = timeString
}

@BindingAdapter("error")
fun TextInputLayout.setErrorMessage(error: String?) {
    this.error = error
}