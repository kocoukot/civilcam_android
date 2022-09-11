package com.civilcam.ui.alerts.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ui.MainActivity
import com.civilcam.ui.alerts.list.model.AlertListRoute
import com.civilcam.ui.alerts.map.LiveMapFragment
import com.civilcam.ui.common.SupportBottomBar
import com.civilcam.ui.common.ext.navController
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlertsListFragment : Fragment(R.layout.fragment_alerts),
    SupportBottomBar {
    private val viewModel: AlertsListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).showBottomNavBar(true)
        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                AlertListRoute.GoMyProfile -> navController.navigate(R.id.userProfileFragment)
                AlertListRoute.GoSettings -> navController.navigate(R.id.action_alerts_root_to_settingsFragment)
                is AlertListRoute.GoUserAlert -> navController.navigate(
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