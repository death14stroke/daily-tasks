package com.andruid.magic.dailytasks.ui.util

object Validator {
    fun isValidUsername(username: String?) = username.isNullOrBlank().not()
}