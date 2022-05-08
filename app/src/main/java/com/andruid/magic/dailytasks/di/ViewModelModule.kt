package com.andruid.magic.dailytasks.di

import com.andruid.magic.dailytasks.viewmodel.AuthViewModel
import com.andruid.magic.dailytasks.viewmodel.SignupViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SignupViewModel(get(), get()) }
    viewModel { AuthViewModel(get()) }
}