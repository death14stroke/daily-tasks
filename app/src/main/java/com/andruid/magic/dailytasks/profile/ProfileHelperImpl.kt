package com.andruid.magic.dailytasks.profile

import android.content.Context
import android.os.Environment
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.andruid.magic.dailytasks.data.model.ProfileImage
import com.andruid.magic.dailytasks.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

class ProfileHelperImpl(private val context: Context) : ProfileHelper {
    companion object {
        private const val FILE_PROFILE_IMAGE = "profile_image.jpg"
        private const val DATASTORE_USER = "user"
        private val KEY_USERNAME = stringPreferencesKey("user_name")
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_USER)
    }

    override val profileImageFile by lazy {
        File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            FILE_PROFILE_IMAGE
        )
    }

    override suspend fun saveProfile(username: String, profileImage: ProfileImage) {
        saveUsername(username)

        when (profileImage) {
            is ProfileImage.Gallery -> copyGalleryImageToUri(profileImage)
            is ProfileImage.Camera -> {}
            is ProfileImage.None -> clearProfileImage()
        }
    }

    override fun getUser(): Flow<User?> {
        return context.dataStore.data.map { preferences ->
            val userName = preferences[KEY_USERNAME] ?: return@map null
            val profileImagePath = profileImageFile.path

            User(userName, profileImagePath)
        }
    }

    private suspend fun saveUsername(userName: String) {
        context.dataStore.edit { user -> user[KEY_USERNAME] = userName }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun copyGalleryImageToUri(profileImage: ProfileImage.Gallery) {
        withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(profileImage.uri).use { input ->
                profileImageFile.outputStream().use { output -> input?.copyTo(output) }
            }
        }
    }

    private suspend fun clearProfileImage() {
        withContext(Dispatchers.IO) { profileImageFile.delete() }
    }
}