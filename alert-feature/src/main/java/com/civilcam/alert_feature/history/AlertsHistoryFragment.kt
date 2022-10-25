package com.civilcam.alert_feature.history

import androidx.compose.runtime.Composable
import com.civilcam.alert_feature.history.model.AlertHistoryRoute
import com.civilcam.ext_features.arch.BaseFragment
import com.civilcam.ext_features.compose.ComposeFragmentRoute
import com.civilcam.ext_features.ext.callPhone
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlertsHistoryFragment : BaseFragment<AlertsHistoryViewModel>() {
    override val viewModel: AlertsHistoryViewModel by viewModel()

    override val screenContent: @Composable (AlertsHistoryViewModel) -> Unit =
        { AlertsListScreenContent(viewModel) }

    override fun observeData(composeRoute: ((ComposeFragmentRoute) -> Unit)?) {
        super.observeData { route ->
            when (route) {
                is AlertHistoryRoute.CallUser -> callPhone(route.phoneNumber)
            }
        }
    }
}