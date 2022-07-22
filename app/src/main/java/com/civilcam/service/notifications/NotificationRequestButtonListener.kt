package com.civilcam.service.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.civilcam.service.notifications.NotificationHelper.Companion.EXTRAS_NOTIFICATION_KEY
import com.civilcam.service.notifications.NotificationHelper.Companion.NOTIFICATION_REQUESTS_ID
import timber.log.Timber

class NotificationRequestButtonListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.i("onNewIntent notify close ${intent?.extras?.getString(EXTRAS_NOTIFICATION_KEY)}")

        intent?.extras?.getString(EXTRAS_NOTIFICATION_KEY)?.let { type ->
            when (NotificationAction.byDescription(type)) {
                NotificationAction.CLOSE_ALERT -> {
                    NotificationHelper.cancelAlertProgress()
                    (context?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                        NotificationHelper.NOTIFICATION_ALERT_ID
                    )
                }

                NotificationAction.DETAIL -> {
                    context?.let {
                        Toast.makeText(it, "Alert Detail Redirect", Toast.LENGTH_SHORT).show()
                        NotificationHelper.cancelAlertProgress()
                        (it.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                            NotificationHelper.NOTIFICATION_ALERT_ID
                        )
                    }
                }

                NotificationAction.CLOSE_REQUEST -> {
                    NotificationHelper.cancelRequestProgress()
                    (context?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                        NOTIFICATION_REQUESTS_ID
                    )
                }
                NotificationAction.DENY -> {
                    context?.let {
                        Toast.makeText(it, "Request was denied", Toast.LENGTH_SHORT).show()
                        NotificationHelper.cancelRequestProgress()
                        (it.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                            NOTIFICATION_REQUESTS_ID
                        )
                    }
                }
                NotificationAction.ACCEPT -> {
                    context?.let {
                        Toast.makeText(it, "Request was accepted", Toast.LENGTH_SHORT).show()
                        NotificationHelper.cancelRequestProgress()
                        (it.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                            NOTIFICATION_REQUESTS_ID
                        )
                    }
                }
                else -> {}
            }
        }
    }
}