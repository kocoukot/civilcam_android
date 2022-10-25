package com.civilcam.alert_feature.list

import androidx.compose.runtime.Composable
import com.civilcam.ext_features.SupportBottomBar
import com.civilcam.ext_features.arch.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlertsListFragment : BaseFragment<AlertsListViewModel>(), SupportBottomBar {
    override val viewModel: AlertsListViewModel by viewModel()

    override val screenContent: @Composable (AlertsListViewModel) -> Unit = {
        AlertsListScreenContent(viewModel)
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshList()
        viewModel.loadAvatar()
    }
}