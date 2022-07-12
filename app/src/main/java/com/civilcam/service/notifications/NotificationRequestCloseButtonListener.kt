package com.civilcam.service.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.civilcam.service.notifications.NotificationHelper.Companion.NOTIFICATION_REQUESTS_ID
import timber.log.Timber

class NotificationRequestCloseButtonListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.i("onNewIntent notify close ${intent?.extras?.getString("alert_type")}")
        NotificationHelper.cancelRequestProgress()
        (context?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            NOTIFICATION_REQUESTS_ID
        )
    }
}