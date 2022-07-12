package com.civilcam.service.notifications

import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.civilcam.R
import com.civilcam.domain.model.settings.NotificationType
import com.civilcam.service.NotificationAcceptButtonListener
import com.civilcam.service.NotificationCloseButtonListener
import com.civilcam.service.NotificationDenyButtonListener
import org.koin.core.component.KoinComponent
import timber.log.Timber


class NotificationHelper : KoinComponent {

    fun showRequestNotification(context: Context) {
        val name = "Alleria Windrunner"
        val maxProgress = 5
        var mProgressStatus = 0
        showProgress = true
        val channelId = "${context.packageName}-${NotificationType.REQUESTS.notifyName}"
        val notificationTitle = context.getString(R.string.notification_request_title)
        val notificationText = openHoursText(
            name = name,
            text = context.getString(R.string.notification_request_text),
            context = context,
        )


        val notificationLayout = RemoteViews(context.packageName, R.layout.view_notification_small)
        notificationLayout.setTextViewText(R.id.notification_title, notificationTitle)
        notificationLayout.setTextViewText(R.id.notification_content, notificationText)

        val notificationLayoutExpanded =
            RemoteViews(context.packageName, R.layout.view_notification_big)

        notificationLayoutExpanded.setTextViewText(R.id.notification_title, notificationTitle)
        notificationLayoutExpanded.setTextViewText(R.id.notification_content, notificationText)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)

        val broadcastIntentClose = Intent(context, NotificationCloseButtonListener::class.java)
        val broadcastIntentAccept = Intent(context, NotificationAcceptButtonListener::class.java)
        val broadcastIntentDeny = Intent(context, NotificationDenyButtonListener::class.java)

        val broadcastPendingIntentClose =
            PendingIntent.getBroadcast(context, 0, broadcastIntentClose, 0)
        val broadcastPendingIntentDeny =
            PendingIntent.getBroadcast(context, 0, broadcastIntentDeny, 0)
        val broadcastPendingIntentAccept =
            PendingIntent.getBroadcast(context, 0, broadcastIntentAccept, 0)



        notificationBuilder.apply {
            setOnlyAlertOnce(true)
            setCustomContentView(notificationLayout)
            setCustomBigContentView(notificationLayoutExpanded)
            contentView.setProgressBar(R.id.progressBar, maxProgress, 0, false)
            bigContentView.setProgressBar(R.id.progressBar, maxProgress, 0, false)
            setProgress(maxProgress, 0, false)
            setSmallIcon(R.mipmap.ic_launcher)
            setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setOngoing(false)

            setAutoCancel(true)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
            priority = NotificationCompat.PRIORITY_HIGH


            notificationBuilder.contentView.apply {
                setOnClickPendingIntent(R.id.button_close_notification, broadcastPendingIntentClose)

                setOnClickPendingIntent(R.id.button_notification_deny, broadcastPendingIntentDeny)

                setOnClickPendingIntent(
                    R.id.button_notification_accept,
                    broadcastPendingIntentAccept
                )
            }


            notificationBuilder.bigContentView.apply {
                setOnClickPendingIntent(R.id.button_close_notification, broadcastPendingIntentClose)
                setOnClickPendingIntent(R.id.button_notification_deny, broadcastPendingIntentDeny)
                setOnClickPendingIntent(
                    R.id.button_notification_accept,
                    broadcastPendingIntentAccept
                )
            }
        }

        NotificationManagerCompat.from(context).apply {
            while (mProgressStatus <= maxProgress && showProgress) {
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

                notify(NOTIFICATION_REQUESTS_ID, notificationBuilder.build())
                Thread.sleep(1000)
                mProgressStatus++
            }

        }
    }

    private fun openHoursText(
        name: String,
        text: String,
        context: Context
    ): SpannableStringBuilder {
        val wholeText = "$name $text"
        return SpannableStringBuilder(wholeText).apply {
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.primaryRed
                    )
                ),
                0,
                name.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.grayText
                    )
                ),
                name.length + 1,
                wholeText.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
    }

    companion object {
        const val NOTIFICATION_REQUESTS_ID = 1002
        const val NOTIFICATION_ALARM_ID = 1003
        private var showProgress = true

        fun cancelProgress() {
            showProgress = false
        }

        fun createNotificationChannel(
            context: Context,
            name: String,
            description: String,
        ) {
            val channelId = "${context.packageName}-$name"
            val channel =
                NotificationChannel(channelId, name, NotificationManagerCompat.IMPORTANCE_HIGH)
            channel.description = description
            channel.lockscreenVisibility = VISIBILITY_PUBLIC
            channel.setShowBadge(true)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}