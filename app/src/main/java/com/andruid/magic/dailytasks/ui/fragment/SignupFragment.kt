package com.andruid.magic.dailytasks.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.request.CachePolicy
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.data.MENU_ITEM
import com.andruid.magic.dailytasks.data.model.MenuItem
import com.andruid.magic.dailytasks.data.model.ProfileImage
import com.andruid.magic.dailytasks.databinding.FragmentSignupBinding
import com.andruid.magic.dailytasks.ui.util.snackBar
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.andruid.magic.dailytasks.viewmodel.SignupViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignupFragment : Fragment(R.layout.fragment_signup) {
    private val binding by viewBinding(FragmentSignupBinding::bind)
    private val navController by lazy { findNavController() }
    private val viewModel by viewModel<SignupViewModel>()
    private val imageUri by lazy {
        val authority = "${requireActivity().applicationContext.packageName}.provider"
        FileProvider.getUriForFile(requireContext(), authority, viewModel.getProfileImageFile())
    }
    private val selectImageContract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            if (imageUri == null)
                return@registerForActivityResult

            viewModel.selectGalleryImage(imageUri)
        }
    private val takePictureContract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (!success)
                return@registerForActivityResult

            viewModel.selectCameraImage()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(MENU_ITEM) { _, bundle ->
            when (MenuItem.values()[bundle.getInt(MENU_ITEM)]) {
                MenuItem.MENU_CAMERA -> takePictureContract.launch(imageUri)
                MenuItem.MENU_GALLERY -> selectImageContract.launch("image/*")
                MenuItem.MENU_DELETE -> viewModel.removeProfileImage()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.profileImageLiveData.observe(viewLifecycleOwner) {
            val image = when (it) {
                is ProfileImage.Gallery -> it.uri
                is ProfileImage.Camera -> imageUri
                is ProfileImage.None -> R.drawable.user
            }

            binding.profileIv.load(image) { memoryCachePolicy(CachePolicy.DISABLED) }
        }
    }

    private fun initListeners() {
        binding.profileIv.setOnClickListener {
            navController.navigate(SignupFragmentDirections.actionSignupFragmentToSelectAvatarBottomSheetDialogFragment())
        }

        binding.proceedBtn.setOnClickListener {
            lifecycleScope.launch {
                viewModel.saveProfile { isSuccess ->
                    if (!isSuccess)
                        binding.root.snackBar(R.string.profile_error)
                }
            }
        }
    }
}