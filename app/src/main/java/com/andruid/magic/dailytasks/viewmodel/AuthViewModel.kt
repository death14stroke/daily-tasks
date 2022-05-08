package com.andruid.magic.dailytasks.viewmodel

import androidx.lifecycle.ViewModel
import com.andruid.magic.dailytasks.repository.MainRepository

class AuthViewModel(private val repository: MainRepository) : ViewModel() {
    fun getCurrentUser() = repository.getUser()
}