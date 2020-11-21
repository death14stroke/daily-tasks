package com.andruid.magic.dailytasks.util

import android.content.Context
import com.andruid.magic.dailytasks.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun Context.showCompleteTaskDialog(): Boolean {
    lateinit var result: Continuation<Boolean>

    MaterialAlertDialogBuilder(this)
        .setTitle(R.string.dialog_task_complete_title)
        .setMessage(R.string.dialog_task_complete_body)
        .setPositiveButton(R.string.yes) { _, _ ->
            result.resume(true)
        }
        .setNegativeButton(R.string.no) { _, _ ->
            result.resume(false)
        }
        .create()
        .show()

    return suspendCoroutine { continuation -> result = continuation }
}