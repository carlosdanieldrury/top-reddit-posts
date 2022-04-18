package com.drury.topredditposts

import android.app.Application
import com.drury.topredditposts.di.createKoinConfiguration
import org.koin.core.component.KoinComponent

class DefaultApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        startDependencyInjection()
    }

    private fun startDependencyInjection() {
        createKoinConfiguration(context = this)
    }
}