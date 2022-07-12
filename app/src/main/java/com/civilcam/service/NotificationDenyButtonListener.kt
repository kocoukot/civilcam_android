package com.civilcam.service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.civilcam.service.notifications.NotificationHelper
import com.civilcam.service.notifications.NotificationHelper.Companion.NOTIFICATION_REQUESTS_ID
import timber.log.Timber

class NotificationDenyButtonListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.i("onNewIntent notify deny")
        context?.let {
            Toast.makeText(it, "Request was denied", Toast.LENGTH_SHORT).show()
            NotificationHelper.cancelProgress()
            (it.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                NOTIFICATION_REQUESTS_ID
            )
        }
    }
}