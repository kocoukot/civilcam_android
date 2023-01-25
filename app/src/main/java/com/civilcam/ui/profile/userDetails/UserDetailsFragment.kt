package com.civilcam.ui.profile.userDetails

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import com.civilcam.ext_features.arch.BaseFragment
import com.civilcam.ext_features.requireArg
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserDetailsFragment : BaseFragment<UserDetailsViewModel>() {
    private val userId by requireArg<Int>(ARG_USER_ID)

    override val viewModel: UserDetailsViewModel by viewModel {
        parametersOf(userId)
    }

    override val screenContent: @Composable (UserDetailsViewModel) -> Unit = {
        UserDetailsScreenContent(viewModel)
    }

    companion object {
        private const val ARG_USER_ID = "user_id"

        fun createArgs(userId: Int) = bundleOf(
            ARG_USER_ID to userId
        )
    }
}