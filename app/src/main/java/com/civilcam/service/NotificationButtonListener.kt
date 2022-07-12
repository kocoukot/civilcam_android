package com.civilcam.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class NotificationButtonListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.i("onNewIntent $intent")
    }
}