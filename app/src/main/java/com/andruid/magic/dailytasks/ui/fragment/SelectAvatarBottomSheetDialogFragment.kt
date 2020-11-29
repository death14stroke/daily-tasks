package com.andruid.magic.dailytasks.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import com.andruid.magic.dailytasks.databinding.BottomsheetSelectAvatarBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectAvatarBottomSheetDialogFragment(
    private val menuItemClickListener: MenuItemClickListener
) : BottomSheetDialogFragment() {
    companion object {
        @IntDef(MENU_CAMERA, MENU_GALLERY, MENU_DELETE)
        @Retention(AnnotationRetention.SOURCE)
        annotation class MenuItem

        const val MENU_CAMERA = 0
        const val MENU_GALLERY = 1
        const val MENU_DELETE = 2

        fun newInstance(menuItemClickListener: MenuItemClickListener) = SelectAvatarBottomSheetDialogFragment(menuItemClickListener)
    }

    private lateinit var binding: BottomsheetSelectAvatarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetSelectAvatarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.menuGallery.setOnClickListener {
            menuItemClickListener.onMenuItemClick(MENU_GALLERY)
            dismiss()
        }

        binding.menuCamera.setOnClickListener {
            menuItemClickListener.onMenuItemClick(MENU_CAMERA)
            dismiss()
        }

        binding.menuDelete.setOnClickListener {
            menuItemClickListener.onMenuItemClick(MENU_DELETE)
            dismiss()
        }
    }

    interface MenuItemClickListener {
        fun onMenuItemClick(@MenuItem menuItem: Int)
    }
}