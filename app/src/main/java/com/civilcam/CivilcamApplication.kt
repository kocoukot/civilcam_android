package com.civilcam

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.civilcam.di.*
import com.civilcam.di.source.sourceModule
import com.civilcam.domainLayer.model.user.NotificationType
import com.civilcam.domainLayer.usecase.auth.SaveFcmTokenUseCase
import com.civilcam.service.CCFireBaseMessagingService
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

//
//
//
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
        CCFireBaseMessagingService.createNotificationChannel(
            this,
            NotificationType.REQUESTS.notifyName,
            "Notification channel for requests."
        )
        CCFireBaseMessagingService.createNotificationChannel(
            this,
            NotificationType.ALERTS.notifyName,
            "Notification channel for alerts"
        )

        CCFireBaseMessagingService.createNotificationChannel(
            this,
            NotificationType.LOCATION.notifyName,
            "Notification channel for location"
        )


        CCFireBaseMessagingService.createNotificationChannel(
            this,
            NotificationType.COMMON.notifyName,
            "Notification channel for common"
        )

        val channel = NotificationChannel(
            NotificationType.LOCATION.notifyName,
            "Location",
            NotificationManager.IMPORTANCE_LOW
        )
            .apply { setShowBadge(false) }

        val downloadChannel = NotificationChannel(
            NotificationType.DOWNLOAD.notifyName,
            "Download",
            NotificationManager.IMPORTANCE_LOW
        )
            .apply { setShowBadge(false) }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
        notificationManager.createNotificationChannel(downloadChannel)
    }


    companion object {
        lateinit var instance: CivilcamApplication
            private set
    }
}