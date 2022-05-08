package com.andruid.magic.dailytasks.application

import android.app.Application
import com.andruid.magic.dailytasks.repository.ProfileRepository
import com.andruid.magic.dailytasks.database.TaskRepository
import com.andruid.magic.dailytasks.di.repoModule
import com.andruid.magic.dailytasks.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

@Suppress("unused")
class DailyTasksApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DailyTasksApplication)
            modules(repoModule, viewModelModule)
        }

        TaskRepository.init(this)
        ProfileRepository.init(this)
    }
}