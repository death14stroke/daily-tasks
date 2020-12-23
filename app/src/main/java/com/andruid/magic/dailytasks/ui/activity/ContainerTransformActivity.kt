package com.andruid.magic.dailytasks.ui.activity

import android.app.Instrumentation
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

abstract class ContainerTransformActivity(
    private val transitionName: String
) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        findViewById<View>(android.R.id.content).transitionName = transitionName
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 500L
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 500L
        }

        super.onCreate(savedInstanceState)
    }

    override fun onStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !isFinishing)
            Instrumentation().callActivityOnSaveInstanceState(this, Bundle())

        super.onStop()
    }

    fun initCollapsingActionBar(toolbar: Toolbar, @ColorInt statusBarColor: Int) {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        window.statusBarColor = statusBarColor
    }
}