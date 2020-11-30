package com.andruid.magic.dailytasks.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.request.CachePolicy
import com.andruid.magic.dailytasks.repository.ProfileRepository
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.databinding.ActivitySignupBinding
import com.andruid.magic.dailytasks.ui.fragment.SelectAvatarBottomSheetDialogFragment
import com.andruid.magic.dailytasks.ui.fragment.SelectAvatarBottomSheetDialogFragment.Companion.MENU_CAMERA
import com.andruid.magic.dailytasks.ui.fragment.SelectAvatarBottomSheetDialogFragment.Companion.MENU_DELETE
import com.andruid.magic.dailytasks.ui.fragment.SelectAvatarBottomSheetDialogFragment.Companion.MENU_GALLERY
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity(),
    SelectAvatarBottomSheetDialogFragment.MenuItemClickListener {
    private val binding by viewBinding(ActivitySignupBinding::inflate)
    private val imageUri by lazy {
        val authority = "${applicationContext.packageName}.provider"
        FileProvider.getUriForFile(this, authority, ProfileRepository.getProfilePictureFile())
    }
    private val selectImageContract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            if (imageUri == null)
                return@registerForActivityResult

            binding.profileIv.load(imageUri)
            selectedImageUri = imageUri
        }
    private val takePictureContract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (!success)
                return@registerForActivityResult

            binding.profileIv.load(imageUri) {
                memoryCachePolicy(CachePolicy.DISABLED)
            }
        }

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        binding.profileIv.setOnClickListener {
            SelectAvatarBottomSheetDialogFragment.newInstance(this).run {
                show(supportFragmentManager, "add_photo_bottom_sheet_dialog")
            }
        }

        binding.proceedBtn.setOnClickListener {
            val userName = binding.nameEt.text.toString().trim()
            if (userName.isBlank()) {
                binding.nameInput.error = "Username cannot be blank"
                return@setOnClickListener
            }

            lifecycleScope.launch { ProfileRepository.saveUsername(userName) }
            selectedImageUri?.let { imageUri ->
                lifecycleScope.launch {
                    ProfileRepository.copyImageFromContentUri(imageUri)
                }
            }

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onMenuItemClick(menuItem: Int) {
        when (menuItem) {
            MENU_GALLERY -> {
                selectImageContract.launch("image/*")
            }
            MENU_CAMERA -> {
                takePictureContract.launch(imageUri)
                selectedImageUri = null
            }
            MENU_DELETE -> {
                binding.profileIv.load(R.drawable.user)
                selectedImageUri = null
            }
        }
    }
}