package com.civilcam.ui.network.inviteByNumber

import androidx.compose.runtime.Composable
import com.civilcam.ext_features.arch.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class InviteByNumberFragment : BaseFragment<InviteByNumberViewModel>() {

    override val viewModel: InviteByNumberViewModel by viewModel()

    override val screenContent: @Composable (InviteByNumberViewModel) -> Unit = {
        InviteByNumberScreenContent(viewModel)
    }
}