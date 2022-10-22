package com.civilcam.ui.profile.userDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ext_features.requireArg
import com.civilcam.ui.profile.userDetails.model.UserDetailsRoute
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserDetailsFragment : Fragment() {
    private val viewModel: UserDetailsViewModel by viewModel {
        parametersOf(userId)
    }

    private val userId by requireArg<Int>(ARG_USER_ID)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                UserDetailsRoute.GoBack -> navController.popBackStack()
            }
        }


        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )

            setContent {
                UserDetailsScreenContent(viewModel)
            }
        }
    }

    companion object {
        private const val ARG_USER_ID = "user_id"

        fun createArgs(userId: Int) = bundleOf(
            ARG_USER_ID to userId
        )
    }
}