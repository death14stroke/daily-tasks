package com.andruid.magic.dailytasks.repository

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.andruid.magic.dailytasks.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

object ProfileRepository {
    private const val DATASTORE_USER = "user"
    private const val FILE_PROFILE_IMAGE = "profile_image.jpg"

    private val KEY_USERNAME = preferencesKey<String>("user_name")

    private lateinit var context: Context

    fun init(application: Application) {
        context = application.applicationContext
    }

    suspend fun saveUsername(userName: String) {
        val dataStore = context.createDataStore(name = DATASTORE_USER)
        dataStore.edit { user ->
            user[KEY_USERNAME] = userName
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun copyImageFromContentUri(sourceUri: Uri) {
        val destFile = getProfilePictureFile()

        withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(sourceUri).use { input ->
                destFile.outputStream().use { output ->
                    input?.copyTo(output)
                }
            }
        }
    }

    fun getUser(): Flow<User?> {
        val dataStore = context.createDataStore(name = DATASTORE_USER)
        return dataStore.data.map { preferences ->
            val userName = preferences[KEY_USERNAME] ?: return@map null
            val profileImagePath = getProfilePictureFile().path

            User(userName, profileImagePath)
        }
    }

    fun getProfilePictureFile(): File {
        return File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), FILE_PROFILE_IMAGE)
    }
}