package com.civilcam.alert_feature.history

import android.content.Intent
import androidx.compose.runtime.Composable
import com.civilcam.alert_feature.history.model.AlertHistoryRoute
import com.civilcam.ext_features.arch.BaseFragment
import com.civilcam.ext_features.compose.ComposeFragmentRoute
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AlertsHistoryFragment : BaseFragment<AlertsHistoryViewModel>() {
    override val viewModel: AlertsHistoryViewModel by viewModel()

    override val screenContent: @Composable (AlertsHistoryViewModel) -> Unit =
        { AlertsListScreenContent(viewModel) }

    override fun observeData(composeRoute: ((ComposeFragmentRoute) -> Unit)?) {
        super.observeData { route ->
            when (route) {
                is AlertHistoryRoute.OpenVideo -> {
                    Timber.tag("video").i("videoUri ${route.videoUri}")
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = route.videoUri
                        type = "video/mp4"

                    }
                    startActivity(intent)
                }
            }
        }
    }
}