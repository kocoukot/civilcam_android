package com.civilcam.settings_feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.civilcam.ext_features.ext.navController
import com.civilcam.ext_features.ext.navigateToStart
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.settings_feature.model.SettingsRoute
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                SettingsRoute.GoBack -> navController.popBackStack()
                SettingsRoute.GoTerms -> navController.navigate(
                    "${getString(com.civilcam.ext_features.R.string.direction_termsFragment)}/${true}".toUri(),
                )
                SettingsRoute.ForceLogout, SettingsRoute.GoStartScreen -> navigateToStart()
                SettingsRoute.GoSubManage -> navController.navigate(
                    "${getString(com.civilcam.ext_features.R.string.direction_subscriptionFragment)}/${true}".toUri(),
                )
            }
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    viewLifecycleOwner
                )
            )
            setContent {
                SettingsScreenContent(viewModel)
            }
        }
    }

}