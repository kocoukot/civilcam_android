package com.civilcam.alert_feature.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.alert_feature.history.model.AlertHistoryRoute
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlertsHistoryFragment : Fragment() {
    private val viewModel: AlertsHistoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                AlertHistoryRoute.GoBack -> navController.popBackStack()
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