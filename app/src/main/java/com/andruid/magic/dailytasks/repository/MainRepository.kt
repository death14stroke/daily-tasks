package com.andruid.magic.dailytasks.repository

import com.andruid.magic.dailytasks.data.model.ProfileImage
import com.andruid.magic.dailytasks.profile.ProfileHelper

class MainRepository(private val profileHelper: ProfileHelper) {
    fun getProfileImageFile() = profileHelper.profileImageFile

    suspend fun saveProfile(username: String, profileImage: ProfileImage) {
        profileHelper.saveProfile(username, profileImage)
    }

    fun getUser() = profileHelper.getUser()
}