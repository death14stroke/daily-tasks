package com.andruid.magic.dailytasks.util

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.core.content.getSystemService


object ScreenUtils {
    @Suppress("DEPRECATION")
    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService<WindowManager>()!!

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    fun dpToPx(context: Context, value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}