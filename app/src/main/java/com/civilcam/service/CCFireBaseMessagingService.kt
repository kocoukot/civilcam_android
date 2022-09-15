package com.civilcam.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.civilcam.R
import com.civilcam.domainLayer.usecase.auth.SaveFcmTokenUseCase
import com.civilcam.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject
import timber.log.Timber


class CCFireBaseMessagingService : FirebaseMessagingService() {

    private val saveFcmTokenUseCase: SaveFcmTokenUseCase by inject()

    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("create a new FCM Token $token")
        // Need to safe token in preference to take it when make log in or sinUP
        saveFcmTokenUseCase(token)
    }

    // To show Notification in Foreground
    @SuppressLint("UnspecifiedImmutableFlag", "ResourceAsColor")
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Timber.d("Message Recived " + message.notification?.body)

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent
            .getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )


        val channelId = getString(R.string.app_name)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setNumber(notificationManager.activeNotifications.count())
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setStyle(NotificationCompat.BigTextStyle())

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            channelId,
            "Default channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.setShowBadge(true)
        manager.createNotificationChannel(channel)

        val notification = builder.build()
        manager.notify(0, notification)
    }
}