package com.civilcam

import android.app.Application
import com.civilcam.di.*
import com.civilcam.di.source.sourceModule
import com.civilcam.domainLayer.model.settings.NotificationType
import com.civilcam.service.notifications.NotificationHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class CivilcamApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        instance = this
        startTimber()
        gleapInit()
        createNotificationChannels()

        startKoin {
            androidLogger()
            androidContext(this@CivilcamApplication)
            modules(*presentationModules + sourceModule + storageModule + repositoryModule + domainModule + networkModule)
        }
    }

    private fun startTimber() = Timber.plant(Timber.DebugTree())

    private fun gleapInit() {
        if (BuildConfig.DEBUG) {
            //   Gleap.initialize(BuildConfig.GLEAP_KEY, this)
        }
    }

    private fun createNotificationChannels() {
        NotificationHelper.createNotificationChannel(
            this,
            NotificationType.REQUESTS.notifyName,
            "Notification channel for requests."
        )
        NotificationHelper.createNotificationChannel(
            this,
            NotificationType.ALERTS.notifyName,
            "Notification channel for alerts"
        )
    }


    companion object {
        lateinit var instance: CivilcamApplication
            private set
    }
}


