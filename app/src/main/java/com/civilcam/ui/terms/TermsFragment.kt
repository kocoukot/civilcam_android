package com.civilcam.ui.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.terms.model.TermsRoute
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TermsFragment : Fragment() {
    private val viewModel: TermsViewModel by viewModel {
        parametersOf(isSettings)
    }

    private val isSettings = false // by requireArg<Boolean>(ARG_IS_SETTINGS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                TermsRoute.GoBack -> navController.popBackStack()
                TermsRoute.GoSubscription -> Toast.makeText(
                    requireContext(),
                    "To subscription",
                    Toast.LENGTH_SHORT
                ).show()
                is TermsRoute.GoWebView -> Toast.makeText(
                    requireContext(),
                    "To webView",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    viewLifecycleOwner
                )
            )

            setContent {
                TermsScreenContent(viewModel)
            }
        }
    }

    companion object {
        private const val ARG_IS_SETTINGS = "is_settings"

        fun createArgs(isSettings: Boolean) = bundleOf(
            ARG_IS_SETTINGS to isSettings
        )
    }
}