package com.andruid.magic.dailytasks.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.data.model.ProfileImage
import com.andruid.magic.dailytasks.repository.MainRepository
import com.andruid.magic.dailytasks.ui.util.Validator

class SignupViewModel(
    private val repository: MainRepository, application: Application
) : AndroidViewModel(application) {
    private val _profileImageLiveData = MutableLiveData<ProfileImage>(ProfileImage.None)
    val profileImageLiveData: LiveData<ProfileImage> = _profileImageLiveData
    val usernameLiveData = MutableLiveData<String>()
    val errorLiveData = usernameLiveData.map {
        if (!Validator.isValidUsername(it))
            application.getString(R.string.username_input_error)
        else
            null
    }

    fun getProfileImageFile() = repository.getProfileImageFile()

    fun selectGalleryImage(uri: Uri) {
        _profileImageLiveData.value = ProfileImage.Gallery(uri)
    }

    fun selectCameraImage() {
        _profileImageLiveData.value = ProfileImage.Camera
    }

    fun removeProfileImage() {
        _profileImageLiveData.value = ProfileImage.None
    }

    suspend fun saveProfile(onSaveListener: (isSuccess: Boolean) -> Unit) {
        if (errorLiveData.value == null) {
            try {
                repository.saveProfile(
                    usernameLiveData.value!!,
                    _profileImageLiveData.value!!
                )
                onSaveListener(true)
            } catch (e: Exception) {
                onSaveListener(false)
            }
        }
    }
}