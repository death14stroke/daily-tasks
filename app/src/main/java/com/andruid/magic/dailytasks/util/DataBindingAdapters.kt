package com.andruid.magic.dailytasks.util

import androidx.databinding.BindingAdapter
import com.andruid.magic.dailytasks.ui.custom.timepickerinput.formatTime
import com.google.android.material.textview.MaterialTextView

@BindingAdapter("showTime")
fun MaterialTextView.showTimeFromMillis(millis: Long) {
    val timeString = formatTime(millis)
    text = timeString
}