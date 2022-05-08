package com.andruid.magic.dailytasks.profile

import com.andruid.magic.dailytasks.data.model.ProfileImage
import com.andruid.magic.dailytasks.data.model.User
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ProfileHelper {
    val profileImageFile: File

    suspend fun saveProfile(username: String, profileImage: ProfileImage)

    fun getUser(): Flow<User?>
}