package com.civilcam.ui.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.terms.model.TermsRoute
import com.civilcam.ui.terms.webView.WebViewFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TermsFragment : Fragment() {
    private val viewModel: TermsViewModel by viewModel {
        parametersOf(isSettings)
    }

    private val isSettings = false // by requireArg<Boolean>(ARG_IS_SETTINGS) // todo fix later

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                TermsRoute.GoBack -> navController.popBackStack()
                TermsRoute.GoSubscription -> navController.navigate(R.id.testFragment)
//                    Toast.makeText(
//                    requireContext(),
//                    "To subscription",
//                    Toast.LENGTH_SHORT
//                ).show()
                is TermsRoute.GoWebView -> navController.navigate(
                    R.id.action_termsFragment_to_webViewFragment,
                    WebViewFragment.createArgs(route.webLink)
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
                TermsScreenContent(viewModel)
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        activity?.window?.apply {
//            navigationBarColor = ContextCompat.getColor(requireContext(), R.color.lightGray)
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        activity?.window?.apply {
//            navigationBarColor = ContextCompat.getColor(requireContext(), R.color.white)
//        }
//    }

    companion object {
        private const val ARG_IS_SETTINGS = "is_settings"

        fun createArgs(isSettings: Boolean) = bundleOf(
            ARG_IS_SETTINGS to isSettings
        )
    }
}