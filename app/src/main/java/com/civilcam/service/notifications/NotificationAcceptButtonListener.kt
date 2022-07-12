package com.civilcam.service.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.civilcam.service.notifications.NotificationHelper.Companion.NOTIFICATION_REQUESTS_ID
import timber.log.Timber

class NotificationAcceptButtonListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.i("onNewIntent notify accept")
        context?.let {
            Toast.makeText(it, "Request was accepted", Toast.LENGTH_SHORT).show()
            NotificationHelper.cancelRequestProgress()
            (it.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                NOTIFICATION_REQUESTS_ID
            )
        }
    }
}