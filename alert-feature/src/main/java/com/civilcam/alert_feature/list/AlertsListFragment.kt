package com.civilcam.alert_feature.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.civilcam.alert_feature.R
import com.civilcam.alert_feature.list.model.AlertListRoute
import com.civilcam.alert_feature.map.LiveMapFragment
import com.civilcam.ext_features.SupportBottomBar
import com.civilcam.ext_features.ext.navController
import com.civilcam.ext_features.live_data.observeNonNull
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlertsListFragment : Fragment(), SupportBottomBar {
    private val viewModel: AlertsListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                AlertListRoute.GoMyProfile -> navController.navigate(getString(com.civilcam.ext_features.R.string.direction_userProfileFragment).toUri())
                AlertListRoute.GoSettings -> navController.navigate(getString(com.civilcam.ext_features.R.string.direction_settingsFragment).toUri())
                is AlertListRoute.GoUserAlert ->
                    navController.navigate(
                        R.id.liveMapFragment,
                        LiveMapFragment.createArgs(route.userId)
                    )
                AlertListRoute.GoAlertHistory -> navController.navigate(R.id.action_alerts_root_to_alertsHistoryFragment)
            }
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    viewLifecycleOwner
                )
            )
            setContent {
                AlertsListScreenContent(viewModel)
            }
        }
    }
}