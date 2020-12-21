package com.andruid.magic.dailytasks.ui.custom

import android.os.Bundle
import com.andruid.magic.dailytasks.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class RoundedBottomSheetDialogFragment : BottomSheetDialogFragment() {
    override fun getTheme() = R.style.Theme_DailyTasks_BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        BottomSheetDialog(requireContext(), theme)
}