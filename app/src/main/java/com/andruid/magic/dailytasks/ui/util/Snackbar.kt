package com.andruid.magic.dailytasks.ui.util

import android.view.View
import androidx.annotation.StringRes
import com.andruid.magic.dailytasks.R
import com.google.android.material.snackbar.Snackbar

fun View.snackBar(@StringRes msgId: Int) {
    val snackbar = Snackbar.make(this, msgId, Snackbar.LENGTH_SHORT)
    snackbar.setAction(R.string.dismiss) {
        snackbar.dismiss()
    }
    snackbar.show()
}