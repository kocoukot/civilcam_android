package com.civilcam.ui.network.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.common.ext.setPan
import com.civilcam.common.ext.setResize
import com.civilcam.ui.common.SupportBottomBar
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.network.main.model.NetworkMainRoute
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
        setPan()
    }

    override fun onStop() {
        super.onStop()
        setResize()
    }
}