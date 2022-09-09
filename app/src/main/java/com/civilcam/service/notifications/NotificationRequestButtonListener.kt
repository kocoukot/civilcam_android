package com.civilcam.service.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.usecase.guardians.SetRequestReactionUseCase
import com.civilcam.service.notifications.NotificationHelper.Companion.EXTRAS_NOTIFICATION_KEY
import com.civilcam.service.notifications.NotificationHelper.Companion.NOTIFICATION_REQUESTS_ID
import com.civilcam.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber


class NotificationRequestButtonListener : KoinComponent, BroadcastReceiver() {
    private val scope = CoroutineScope(SupervisorJob())

    private val setRequestReactionUseCase: SetRequestReactionUseCase by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult: PendingResult = goAsync()

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
                        val activityIntent = Intent(context, MainActivity::class.java)
                        activityIntent.putExtra(ARG_MAP_ALERT_ID, 999)
                        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(activityIntent)

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
                    scope.launch(Dispatchers.IO) {
                        try {
                            setRequestReactionUseCase(ButtonAnswer.DECLINE, 1)
                        } finally {
                            pendingResult.finish()
                        }
                    }
                    context?.let {
                        Toast.makeText(it, "Request was denied", Toast.LENGTH_SHORT).show()
                        NotificationHelper.cancelRequestProgress()
                        (it.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                            NOTIFICATION_REQUESTS_ID
                        )
                    }
                }
                NotificationAction.ACCEPT -> {
                    scope.launch(Dispatchers.IO) {
                        try {
                            setRequestReactionUseCase(ButtonAnswer.ACCEPT, 1)
                        } finally {
                            pendingResult.finish()
                        }
                    }
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

    companion object {
        const val ARG_MAP_ALERT_ID = "map_alert_id"
    }
}