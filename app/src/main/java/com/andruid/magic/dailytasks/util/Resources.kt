package com.andruid.magic.dailytasks.util

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.drawable(@DrawableRes drawableRes: Int) =
    ContextCompat.getDrawable(this, drawableRes)

fun Context.color(@ColorRes colorRes: Int) =
    ContextCompat.getColor(this, colorRes)