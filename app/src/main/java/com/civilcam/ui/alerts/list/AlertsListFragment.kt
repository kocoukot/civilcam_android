package com.civilcam.ui.alerts.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.ui.MainActivity
import com.civilcam.ui.alerts.list.model.AlertListRoute
import com.civilcam.ui.common.SupportBottomBar
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
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
                AlertListRoute.GoMyProfile -> {}
                AlertListRoute.GoSettings -> navController.navigate(R.id.action_alerts_root_to_settingsFragment)
                is AlertListRoute.GoUserProfile -> {}
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