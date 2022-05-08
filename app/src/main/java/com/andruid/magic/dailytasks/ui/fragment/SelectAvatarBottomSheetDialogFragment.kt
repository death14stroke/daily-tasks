package com.andruid.magic.dailytasks.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.data.MENU_ITEM
import com.andruid.magic.dailytasks.data.model.MenuItem
import com.andruid.magic.dailytasks.databinding.BottomsheetSelectAvatarBinding
import com.andruid.magic.dailytasks.ui.custom.RoundedBottomSheetDialogFragment
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding

class SelectAvatarBottomSheetDialogFragment : RoundedBottomSheetDialogFragment() {
    private val binding by viewBinding(BottomsheetSelectAvatarBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottomsheet_select_avatar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.menuCamera.setOnClickListener {
            dismiss()
            sendMenuItemSelection(MenuItem.MENU_CAMERA)
        }

        binding.menuGallery.setOnClickListener {
            dismiss()
            sendMenuItemSelection(MenuItem.MENU_GALLERY)
        }

        binding.menuDelete.setOnClickListener {
            dismiss()
            sendMenuItemSelection(MenuItem.MENU_DELETE)
        }
    }

    private fun sendMenuItemSelection(menuItem: MenuItem) {
        setFragmentResult(MENU_ITEM, bundleOf(MENU_ITEM to menuItem.ordinal))
    }
}