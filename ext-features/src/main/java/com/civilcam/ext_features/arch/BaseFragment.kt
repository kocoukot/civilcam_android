package com.civilcam.ext_features.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.civilcam.ext_features.compose.ComposeFragmentRoute
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {
    protected abstract val screenContent: @Composable ((VM) -> Unit)
    protected abstract val viewModel: VM

    protected open fun observeData(composeRoute: ((ComposeFragmentRoute) -> Unit)? = null) {
        viewModel.observeSteps().onEach { route ->
            route.handleRoute().handleNavigation(this) {
                composeRoute?.invoke(route)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeData()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                screenContent.invoke(viewModel)
            }
        }
    }
}