package com.civilcam.ui.network.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.common.ext.setPan
import com.civilcam.common.ext.setResize
import com.civilcam.ui.MainActivity
import com.civilcam.ui.common.SupportBottomBar
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.network.main.model.NetworkMainRoute
import com.civilcam.ui.profile.userDetails.UserDetailsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class NetworkMainFragment : Fragment(), SupportBottomBar {
    private val viewModel: NetworkMainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                NetworkMainRoute.GoSettings -> navController.navigate(R.id.action_network_root_to_settingsFragment)
                NetworkMainRoute.GoContacts -> navController.navigate(R.id.action_network_root_to_contactsFragment)
                is NetworkMainRoute.GoUserDetail -> navController.navigate(
                    R.id.action_network_root_to_userDetailsFragment,
                    UserDetailsFragment.createArgs(route.userId)
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
                NetworkMainScreenContent(viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomNavBar(true)
        setPan()
    }

    override fun onStop() {
        super.onStop()
        setResize()
    }
}