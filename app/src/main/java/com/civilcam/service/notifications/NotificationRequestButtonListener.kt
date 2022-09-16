package com.civilcam.service.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.usecase.guardians.SetRequestReactionUseCase
import com.civilcam.service.CCFireBaseMessagingService
import com.civilcam.service.CCFireBaseMessagingService.Companion.EXTRAS_NOTIFICATION_ALERT_ID
import com.civilcam.service.CCFireBaseMessagingService.Companion.EXTRAS_NOTIFICATION_KEY
import com.civilcam.service.CCFireBaseMessagingService.Companion.NOTIFICATION_REQUESTS_ID
import com.civilcam.ui.MainActivity
import com.civilcam.ui.MainActivity.Companion.ARG_MAP_ALERT_ID
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
        // val pendingResult: PendingResult = goAsync()


        Timber.i("onNewIntent notify close ${intent?.extras?.getString(EXTRAS_NOTIFICATION_KEY)}")
        intent?.extras?.getString(EXTRAS_NOTIFICATION_KEY)?.let { type ->
            when (NotificationAction.byDescription(type)) {
                NotificationAction.CLOSE_ALERT -> {
                    CCFireBaseMessagingService.cancelAlertProgress()
                    (context?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                        CCFireBaseMessagingService.NOTIFICATION_ALERT_ID
                    )
                }

                NotificationAction.DETAIL -> {
                    context?.let {
                        val alertId = intent.extras?.getInt(EXTRAS_NOTIFICATION_ALERT_ID)
                        Timber.tag("alert notif ID").i("broadcast userId $alertId")

                        val activityIntent = Intent(context, MainActivity::class.java)
                        activityIntent.putExtra(ARG_MAP_ALERT_ID, alertId)
                        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(activityIntent)

                        CCFireBaseMessagingService.cancelAlertProgress()
                        (it.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                            CCFireBaseMessagingService.NOTIFICATION_ALERT_ID
                        )
                    }
                }

                NotificationAction.CLOSE_REQUEST -> {
                    CCFireBaseMessagingService.cancelRequestProgress()
                    (context?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                        NOTIFICATION_REQUESTS_ID
                    )
                }
                NotificationAction.DENY -> {
                    scope.launch(Dispatchers.IO) {
                        try {
                            setRequestReactionUseCase(ButtonAnswer.DECLINE, 1)
                        } finally {
                            //    pendingResult.finish()
                        }
                    }
                    context?.let {
                        CCFireBaseMessagingService.cancelRequestProgress()
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
                            //    pendingResult.finish()
                        }
                    }
                    context?.let {
                        CCFireBaseMessagingService.cancelRequestProgress()
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