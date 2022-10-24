package com.civilcam.settings_feature

import androidx.compose.runtime.Composable
import com.civilcam.ext_features.arch.BaseFragment
import com.civilcam.settings_feature.model.SettingsRoute
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment<SettingsRoute, SettingsViewModel>() {
    override val viewModel: SettingsViewModel by viewModel()

    override val screenContent: @Composable (SettingsViewModel) -> Unit =
        { SettingsScreenContent(viewModel) }
}