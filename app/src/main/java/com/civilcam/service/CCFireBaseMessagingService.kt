package com.civilcam.service

import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.civilcam.R
import com.civilcam.domainLayer.model.user.NotificationType
import com.civilcam.domainLayer.usecase.auth.SaveFcmTokenUseCase
import com.civilcam.service.notifications.NotificationRequestButtonListener
import com.civilcam.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import timber.log.Timber


class CCFireBaseMessagingService : FirebaseMessagingService(), KoinComponent {

    private val saveFcmTokenUseCase: SaveFcmTokenUseCase by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("create a new FCM Token $token")
        // Need to safe token in preference to take it when make log in or sinUP
        saveFcmTokenUseCase(token)
    }

    @Suppress("DEPRECATION")
    override fun handleIntent(intent: Intent) {
        try {
            if (intent.extras != null) {
                val builder = RemoteMessage.Builder("MessagingService")
                for (key in intent.extras!!.keySet()) {
                    builder.addData(key!!, intent.extras!![key].toString())
                }
                onMessageReceived(builder.build())
            } else {
                super.handleIntent(intent)
            }
        } catch (e: Exception) {
            super.handleIntent(intent)
        }
    }

    // To show Notification in Foreground
    @SuppressLint("ResourceAsColor")
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.data.let { data ->
            Timber.tag("alert_notif_ID").d("onNewIntent user id data ${message.data}")

            when {
                data[ARG_NOTICE_TYPE_KEY] == ARG_NOTICE_TYPE_ALERT -> {
                    data[ARG_ALERT_ID_KEY]?.toInt()?.let { alertId ->
//                        Timber.tag("alert_notif_ID").d("onNewIntent user id body data $alertId")
                        showAlertNotification(
                            context = applicationContext,
                            alertId,
                            data[ARG_FULL_NAME_KEY].orEmpty()
                        )
                    }
                }
                data[ARG_NOTICE_TYPE_KEY] == ARG_NOTICE_TYPE_REQUEST -> {
                    data[ARG_REQUEST_ID_KEY]?.toInt()?.let { requestId ->
//                        Timber.tag("alert_notif_ID").d("onNewIntent user id data $requestId")
                        showRequestNotification(
                            context = applicationContext,
                            requestId,
                            data[ARG_FULL_NAME_KEY].orEmpty()
                        )
                    }
                }
                data[ARG_NOTICE_TYPE_KEY] == ARG_NOTICE_TYPE_SAFE -> {
                    data[ARG_FULL_NAME_KEY]?.let { fullName ->
                        val notificationText =
                            applicationContext.getString(R.string.notification_safe_text, fullName)
                        val alertId = data[ARG_ALERT_ID_KEY]?.toInt() ?: 0
                        showCommonNotification(
                            context = applicationContext,
                            notificationText,
                            alertId
                        )

                        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).cancel(
                            NOTIFICATION_ALERT_ID + alertId
                        )
                    }
                }
                else -> {
                 //   showCommonNotification(context = applicationContext, "text notification")
                }
            }
        }
    }

    private fun showCommonNotification(
        context: Context,
        notificationText: String,
        alertId: Int = 0,
    ) {
        val channelId = "${context.packageName}-${NotificationType.COMMON.notifyName}"
        val notificationTitle = context.getString(R.string.notification_alert_title)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
        notificationBuilder.apply {

            setContentTitle(notificationTitle)
            setContentText(notificationText)
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_launcher_foreground
                )
            )

            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setOngoing(false)

            setAutoCancel(true)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
            priority = NotificationManager.IMPORTANCE_HIGH
        }

        NotificationManagerCompat.from(context).apply {
            notify(NOTIFICATION_SAFE_ID, notificationBuilder.build())
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showRequestNotification(context: Context, requestId: Int, userName: String) {
        val maxProgress = 5
        var mProgressStatus = 0
        showRequestProgress = true
        val channelId = "${context.packageName}-${NotificationType.REQUESTS.notifyName}"
        val notificationTitle = context.getString(R.string.notification_request_title)
        val notificationText = alertNotificationText(
            userName = userName,
            msgText = context.getString(R.string.notification_request_text),
            context = context,
        )

        val notificationLayout =
            RemoteViews(context.packageName, R.layout.view_notification_request_small)
        notificationLayout.setTextViewText(R.id.notification_title, notificationTitle)
        notificationLayout.setTextViewText(R.id.notification_content, notificationText)

        val notificationLayoutExpanded =
            RemoteViews(context.packageName, R.layout.view_notification_request_small)
        notificationLayoutExpanded.setTextViewText(R.id.notification_title, notificationTitle)
        notificationLayoutExpanded.setTextViewText(R.id.notification_content, notificationText)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)

        val broadcastIntentClose = Intent(context, NotificationRequestButtonListener::class.java)

        val broadcastPendingIntentClose =
            PendingIntent.getBroadcast(
                context,
                0,
                broadcastIntentClose.apply {
                    putExtra(EXTRAS_NOTIFICATION_KEY, "close_request")
                    putExtra(EXTRAS_NOTIFICATION_REQUEST_ID_KEY, requestId)
                    action = System.currentTimeMillis().toString()
                },
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) FLAG_MUTABLE else FLAG_UPDATE_CURRENT
            )

        val broadcastPendingIntentDeny =
            PendingIntent.getBroadcast(
                context,
                1,
                broadcastIntentClose.apply {
                    putExtra(EXTRAS_NOTIFICATION_KEY, "deny")
                    putExtra(EXTRAS_NOTIFICATION_REQUEST_ID_KEY, requestId)
                    action = System.currentTimeMillis().toString()
                },
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) FLAG_MUTABLE else FLAG_UPDATE_CURRENT
            )

        val broadcastPendingIntentAccept =
            PendingIntent.getBroadcast(
                context,
                2,
                broadcastIntentClose.apply {
                    putExtra(EXTRAS_NOTIFICATION_KEY, "accept")
                    putExtra(EXTRAS_NOTIFICATION_REQUEST_ID_KEY, requestId)
                    action = System.currentTimeMillis().toString()
                },
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) FLAG_MUTABLE else FLAG_UPDATE_CURRENT
            )

        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(EXTRAS_NOTIFICATION_REQUEST_ID_KEY, requestId)
            action = System.currentTimeMillis().toString()
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val clickPendingIntent = PendingIntent.getActivity(
            context,
            55,
            clickIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) FLAG_MUTABLE else FLAG_UPDATE_CURRENT
        )
        notificationBuilder.apply {
            setOnlyAlertOnce(true)
            setCustomContentView(notificationLayout)
            setCustomBigContentView(notificationLayoutExpanded)
            setContentIntent(clickPendingIntent)
            setProgress(maxProgress, 0, false)
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_launcher_foreground
                )
            )

            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setOngoing(false)

            setAutoCancel(true)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
            priority = NotificationManager.IMPORTANCE_HIGH


            contentView.apply {
                setProgressBar(R.id.progressBar, maxProgress, 0, false)

                setOnClickPendingIntent(R.id.button_close_notification, broadcastPendingIntentClose)

                setOnClickPendingIntent(R.id.button_notification_deny, broadcastPendingIntentDeny)

                setOnClickPendingIntent(
                    R.id.button_notification_accept,
                    broadcastPendingIntentAccept
                )
            }
            bigContentView.apply {
                setProgressBar(R.id.progressBar, maxProgress, 0, false)

                setOnClickPendingIntent(R.id.button_close_notification, broadcastPendingIntentClose)
                setOnClickPendingIntent(R.id.button_notification_deny, broadcastPendingIntentDeny)

                setOnClickPendingIntent(
                    R.id.button_notification_accept,
                    broadcastPendingIntentAccept
                )

            }


        }

        NotificationManagerCompat.from(context).apply {
            while (mProgressStatus <= maxProgress && showRequestProgress) {
                Timber.i("mProgressStatus $mProgressStatus")
                notificationBuilder.setProgress(
                    maxProgress,
                    mProgressStatus,
                    false
                )

                notificationBuilder.contentView.setProgressBar(
                    R.id.progressBar,
                    maxProgress,
                    mProgressStatus,
                    false
                )

                notificationBuilder.bigContentView.setProgressBar(
                    R.id.progressBar,
                    maxProgress,
                    mProgressStatus,
                    false
                )

                notify(NOTIFICATION_REQUESTS_ID + requestId, notificationBuilder.build())
                Thread.sleep(1000)
                mProgressStatus++
            }
        }
    }


    @SuppressLint("RestrictedApi")
    private fun showAlertNotification(context: Context, alertId: Int, userName: String) {
        val maxProgress = 5
        var mProgressStatus = 0
        showAlertProgress = true
        val channelId = "${context.packageName}-${NotificationType.ALERTS.notifyName}"
        val notificationTitle = context.getString(R.string.notification_alert_title)
        val notificationText = alertNotificationText(
            userName = userName,
            msgText = context.getString(R.string.notification_alert_text),
            context = context,
        )
        Timber.tag("alert_notif_ID").i("FCM alertId $alertId")

        val notificationLayout =
            RemoteViews(context.packageName, R.layout.view_notification_alert_small)
        notificationLayout.setTextViewText(R.id.notification_title, notificationTitle)
        notificationLayout.setTextViewText(R.id.notification_content, notificationText)

        val notificationLayoutExpanded =
            RemoteViews(context.packageName, R.layout.view_notification_alert_small)
        notificationLayoutExpanded.setTextViewText(R.id.notification_title, notificationTitle)
        notificationLayoutExpanded.setTextViewText(R.id.notification_content, notificationText)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)

        val broadcastIntent = Intent(context, NotificationRequestButtonListener::class.java)
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(EXTRAS_NOTIFICATION_KEY, "detail")
            putExtra(EXTRAS_NOTIFICATION_ALERT_ID, alertId)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addCategory(Intent.CATEGORY_LAUNCHER)
            action = System.currentTimeMillis().toString()
        }

        val broadcastPendingIntentClose =
            PendingIntent.getBroadcast(
                context, 11,
                broadcastIntent.apply {
                    putExtra(EXTRAS_NOTIFICATION_KEY, "close_alert")
                    putExtra(EXTRAS_NOTIFICATION_ALERT_ID, alertId)
                    action = System.currentTimeMillis().toString()
                },
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) FLAG_MUTABLE else FLAG_UPDATE_CURRENT
            )

        val activityPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(activityIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(
                88,
                // mutability flag required when targeting Android12 or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) FLAG_MUTABLE else FLAG_UPDATE_CURRENT
            )
        }


        notificationBuilder.apply {
            setOnlyAlertOnce(true)
            setCustomContentView(notificationLayout)
            setCustomBigContentView(notificationLayoutExpanded)

            setProgress(maxProgress, 0, false)
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_launcher_foreground
                )
            )
            setContentIntent(
                PendingIntent
                    .getActivity(
                        context,
                        88,
                        activityIntent,
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) FLAG_MUTABLE else FLAG_UPDATE_CURRENT
                    )
            )

            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setOngoing(false)

            setAutoCancel(true)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
            priority = NotificationManager.IMPORTANCE_HIGH


            contentView.apply {
                setProgressBar(R.id.progressBar, maxProgress, 0, false)
                setOnClickPendingIntent(R.id.button_close_notification, broadcastPendingIntentClose)

                setOnClickPendingIntent(
                    R.id.button_notification_detail,
                    activityPendingIntent
                )
            }


            bigContentView.apply {
                setProgressBar(R.id.progressBar, maxProgress, 0, false)
                setOnClickPendingIntent(R.id.button_close_notification, broadcastPendingIntentClose)
                setOnClickPendingIntent(
                    R.id.button_notification_detail,
                    activityPendingIntent
                )
            }
        }

        NotificationManagerCompat.from(context).apply {
            while (mProgressStatus <= maxProgress && showAlertProgress) {
                Timber.i("mProgressStatus $mProgressStatus")
                notificationBuilder.setProgress(
                    maxProgress,
                    mProgressStatus,
                    false
                )
                notificationBuilder.bigContentView.setProgressBar(
                    R.id.progressBar,
                    maxProgress,
                    mProgressStatus,
                    false
                )

                notificationBuilder.contentView.setProgressBar(
                    R.id.progressBar,
                    maxProgress,
                    mProgressStatus,
                    false
                )

                notify(NOTIFICATION_ALERT_ID + alertId, notificationBuilder.build())
                Thread.sleep(1000)
                mProgressStatus++
            }
        }
    }


    private fun alertNotificationText(
        userName: String,
        msgText: String,
        context: Context
    ): SpannableStringBuilder {
        val wholeText = "$userName $msgText"
        return SpannableStringBuilder(wholeText).apply {
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.primaryRed
                    )
                ),
                0,
                userName.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.grayText
                    )
                ),
                userName.length + 1,
                wholeText.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
    }


    companion object {
        private const val ARG_NOTICE_TYPE_KEY = "noticeType"
        private const val ARG_ALERT_ID_KEY = "alertId"
        private const val ARG_FULL_NAME_KEY = "fullName"
        private const val ARG_REQUEST_ID_KEY = "requestId"

        private const val ARG_NOTICE_TYPE_ALERT = "alert"
        private const val ARG_NOTICE_TYPE_REQUEST = "request"
        private const val ARG_NOTICE_TYPE_SAFE = "safe"

        const val NOTIFICATION_REQUESTS_ID = 1002
        const val NOTIFICATION_ALERT_ID = 1003
        const val NOTIFICATION_SAFE_ID = 1005
        const val NOTIFICATION_LOCATION_ID = 911

        private var showRequestProgress = true
        private var showAlertProgress = true
        var EXTRAS_NOTIFICATION_KEY = "notification_type"
        var EXTRAS_NOTIFICATION_ALERT_ID = "notification_alert_id"
        var EXTRAS_NOTIFICATION_REQUEST_ID_KEY = "notification_request_id"


        fun cancelRequestProgress() {
            showRequestProgress = false
        }

        fun cancelAlertProgress() {
            showAlertProgress = false
        }

        fun createNotificationChannel(
            context: Context,
            name: String,
            description: String,
        ) {
            val channelId = "${context.packageName}-$name"
            val channel =
                NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH)
            channel.description = description
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.setShowBadge(true)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

    }
}