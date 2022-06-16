package com.civilcam

import android.app.Application
import com.civilcam.di.presentationModules
import com.civilcam.di.source.sourceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class CivilcamApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        instance = this
        startTimber()
        startKoin {
            androidLogger()
            androidContext(this@CivilcamApplication)
            modules(*presentationModules + sourceModule)
        }
    }

    private fun startTimber() = Timber.plant(Timber.DebugTree())

    companion object {
        lateinit var instance: CivilcamApplication
            private set
    }
}


