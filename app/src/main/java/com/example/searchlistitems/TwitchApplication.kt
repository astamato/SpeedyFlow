package com.example.searchlistitems

import android.app.Application
import com.example.searchlistitems.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TwitchApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TwitchApplication)
            modules(appModule)
        }
    }
}
