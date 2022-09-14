package com.civilcam

import android.app.Application
import com.civilcam.di.*
import com.civilcam.di.source.sourceModule
import com.civilcam.domainLayer.model.user.NotificationType
import com.civilcam.domainLayer.usecase.auth.SaveFcmTokenUseCase
import com.civilcam.service.notifications.NotificationHelper
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import io.gleap.Gleap
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class CivilcamApplication : Application() {

    private val saveFcmTokenUseCase: SaveFcmTokenUseCase by inject()


    override fun onCreate() {
        super.onCreate()
        instance = this
        startTimber()
        gleapInit()
        createNotificationChannels()
        startFacebookSDK()
        startKoin {
            androidLogger()
            androidContext(this@CivilcamApplication)
            modules(*presentationModules + sourceModule + storageModule + repositoryModule + domainModule + networkModule)
        }
        startFireBaseNotification()
    }

    private fun startTimber() = Timber.plant(Timber.DebugTree())

    private fun gleapInit() {
        if (BuildConfig.DEBUG) {
            Gleap.initialize(BuildConfig.GLEAP_KEY, this)
        }
    }

    private fun startFacebookSDK() {
        AppEventsLogger.activateApp(this)
    }

    private fun startFireBaseNotification() {
        Timber.d("Start Firebase Notificaiton")
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->

                if (!task.isSuccessful) {
                    Timber.d("Fetching FCM registration token failed ${task.exception}")
                    return@OnCompleteListener
                }

                val token = task.result
                saveFcmTokenUseCase(token)

                Timber.d("FCM TOKEN $token")

            })
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