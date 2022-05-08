package com.andruid.magic.dailytasks.repository

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.andruid.magic.dailytasks.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

object ProfileRepository {
    private const val DATASTORE_USER = "user"
    private const val FILE_PROFILE_IMAGE = "profile_image.jpg"

    private val KEY_USERNAME = stringPreferencesKey("user_name")
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_USER)

    private lateinit var context: Context

    fun init(application: Application) {
        context = application.applicationContext
    }

    suspend fun saveUsername(userName: String) {
        context.dataStore.edit { user ->
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
        return context.dataStore.data.map { preferences ->
            val userName = preferences[KEY_USERNAME] ?: return@map null
            val profileImagePath = getProfilePictureFile().path

            User(userName, profileImagePath)
        }
    }

    fun getProfilePictureFile(): File {
        return File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), FILE_PROFILE_IMAGE)
    }
}