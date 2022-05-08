package com.andruid.magic.dailytasks.data.model

import android.net.Uri

sealed class ProfileImage {
    data class Gallery(val uri: Uri): ProfileImage()
    object Camera: ProfileImage()
    object None: ProfileImage()
}