package com.andruid.magic.dailytasks.di

import com.andruid.magic.dailytasks.profile.ProfileHelper
import com.andruid.magic.dailytasks.profile.ProfileHelperImpl
import com.andruid.magic.dailytasks.repository.MainRepository
import org.koin.dsl.module

val repoModule = module {
    single<ProfileHelper> { return@single ProfileHelperImpl(get()) }
    single { MainRepository(get()) }
}