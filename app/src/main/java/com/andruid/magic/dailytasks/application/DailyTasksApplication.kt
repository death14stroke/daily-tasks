package com.andruid.magic.dailytasks.application

import android.app.Application
import com.andruid.magic.dailytasks.repository.ProfileRepository
import com.andruid.magic.dailytasks.database.TaskRepository

@Suppress("unused")
class DailyTasksApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        TaskRepository.init(this)
        ProfileRepository.init(this)
    }
}