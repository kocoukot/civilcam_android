package com.civilcam.service.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.usecase.guardians.SetRequestReactionUseCase
import com.civilcam.service.CCFireBaseMessagingService
import com.civilcam.service.CCFireBaseMessagingService.Companion.EXTRAS_NOTIFICATION_KEY
import com.civilcam.service.CCFireBaseMessagingService.Companion.EXTRAS_NOTIFICATION_REQUEST_ID_KEY
import com.civilcam.service.CCFireBaseMessagingService.Companion.NOTIFICATION_REQUESTS_ID
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

        Timber.i("onNewIntent notify close ${intent?.extras?.getString(EXTRAS_NOTIFICATION_KEY)}")
        intent?.extras?.getString(EXTRAS_NOTIFICATION_KEY)?.let { type ->
            when (NotificationAction.byDescription(type)) {
                NotificationAction.CLOSE_ALERT -> {
                    CCFireBaseMessagingService.cancelAlertProgress()
                    (context?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                        CCFireBaseMessagingService.NOTIFICATION_ALERT_ID
                    )
                }

                NotificationAction.CLOSE_REQUEST -> {
                    CCFireBaseMessagingService.cancelRequestProgress()
                    (context?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                        NOTIFICATION_REQUESTS_ID
                    )
                }
                NotificationAction.DENY -> {
                    context?.let {
                        CCFireBaseMessagingService.cancelRequestProgress()
                        (it.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                            NOTIFICATION_REQUESTS_ID
                        )
                    }

                    intent.extras?.getInt(EXTRAS_NOTIFICATION_REQUEST_ID_KEY)?.let { requestId ->
                        Timber.tag("alert_notif_ID").i("broadcast userId deny  $requestId")
                        answerRequest(requestId, ButtonAnswer.DECLINE)
                    }
                }
                NotificationAction.ACCEPT -> {
                    context?.let {
                        CCFireBaseMessagingService.cancelRequestProgress()
                        (it.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                            NOTIFICATION_REQUESTS_ID
                        )
                    }

                    intent.extras?.getInt(EXTRAS_NOTIFICATION_REQUEST_ID_KEY)?.let { requestId ->
                        Timber.tag("alert_notif_ID").i("broadcast userId accept $requestId")
                        answerRequest(requestId, ButtonAnswer.ACCEPT)
                    }
                }
                else -> {}
            }
        }
    }

    private fun answerRequest(requestId: Int, answerType: ButtonAnswer) {
        scope.launch(Dispatchers.IO) {
            kotlin.runCatching { setRequestReactionUseCase(answerType, requestId) }
                .onSuccess { }
                .onFailure {
                    Timber.tag("alert_notif_ID").e("request error ${it.localizedMessage}")
                }
        }
    }
}