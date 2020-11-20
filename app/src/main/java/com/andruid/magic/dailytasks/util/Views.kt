package com.andruid.magic.dailytasks.util

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat

fun View.setShadow(
    @ColorRes shadowColor: Int,
    @DimenRes cornerRadius: Int,
    @DimenRes elevation: Int,
    shadowGravity: Int = Gravity.BOTTOM,
    @ColorRes backgroundColorResource: Int = 0
) {
    val resource = context.resources
    val firstLayer = 0
    val ratioTopBottom = 3
    val defaultRatio = 2

    if (background == null && backgroundColorResource == 0) {
        throw RuntimeException("Pass backgroundColorResource or use setBackground")
    }

    val backgroundDrawable = when (background) {
        is ColorDrawable -> background
        is StateListDrawable -> {
            background.current
        }
        else -> throw RuntimeException(
            "${background::class.java.name} " +
                    "is not supported, set background as " +
                    "ColorDrawable or pass background as a resource"
        )
    }

    val cornerRadiusValue = resource.getDimension(cornerRadius)
    val elevationValue = resource.getDimension(elevation).toInt()
    val shadowColorValue = ContextCompat.getColor(context, shadowColor)

    val backgroundColor = if (backgroundColorResource != 0) {
        ContextCompat.getColor(context, backgroundColorResource)
    } else {
        (backgroundDrawable as ColorDrawable).color
    }

    val outerRadius = FloatArray(8) { cornerRadiusValue }

    val directionOfY = when (shadowGravity) {
        Gravity.CENTER -> 0
        Gravity.TOP -> -1 * elevationValue / ratioTopBottom
        Gravity.BOTTOM -> elevationValue / ratioTopBottom
        else -> elevationValue / defaultRatio // Gravity.LEFT & Gravity.RIGHT
    }

    val directionOfX = when (shadowGravity) {
        Gravity.START -> -1 * elevationValue / ratioTopBottom
        Gravity.END -> elevationValue / ratioTopBottom
        else -> 0
    }

    val shapeDrawable = ShapeDrawable()
    shapeDrawable.paint.color = backgroundColor
    shapeDrawable.paint.setShadowLayer(
        cornerRadiusValue / ratioTopBottom,
        directionOfX.toFloat(),
        directionOfY.toFloat(),
        shadowColorValue
    )
    shapeDrawable.shape = RoundRectShape(outerRadius, null, null)

    when (Build.VERSION.SDK_INT) {
        in Build.VERSION_CODES.BASE..Build.VERSION_CODES.O_MR1 -> setLayerType(
            View.LAYER_TYPE_SOFTWARE,
            shapeDrawable.paint
        )
    }

    val drawable = LayerDrawable(arrayOf(shapeDrawable))
    drawable.setLayerInset(
        firstLayer,
        elevationValue,
        elevationValue * defaultRatio,
        elevationValue,
        elevationValue * defaultRatio
    )

    background = drawable
}