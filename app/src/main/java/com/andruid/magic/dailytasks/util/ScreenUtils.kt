package com.andruid.magic.dailytasks.util

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import androidx.core.content.getSystemService

object ScreenUtils {
    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService<WindowManager>()!!
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    fun dpToPx(context: Context, value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}