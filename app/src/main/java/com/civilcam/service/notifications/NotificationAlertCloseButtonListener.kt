package com.civilcam.service.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.civilcam.service.notifications.NotificationHelper.Companion.NOTIFICATION_ALERT_ID

class NotificationAlertCloseButtonListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        NotificationHelper.cancelAlertProgress()
        (context?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            NOTIFICATION_ALERT_ID
        )
    }
}