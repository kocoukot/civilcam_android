package com.civilcam.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.ui.MainActivity
import com.civilcam.ui.common.SupportBottomBar
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.settings.model.SettingsRoute
import com.civilcam.ui.terms.TermsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(), SupportBottomBar {
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).showBottomNavBar(true)
        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                SettingsRoute.GoBack -> navController.popBackStack()
                SettingsRoute.GoTerms -> navController.navigate(
                    R.id.termsFragment,
                    TermsFragment.createArgs(true)
                )
                SettingsRoute.GoLanguageSelect -> {

                    navController.navigate(R.id.action_settingsFragment_to_languageSelectFragment)
                }
            }
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    viewLifecycleOwner
                )
            )
            setContent {
                SettingsScreenContent(viewModel)
            }
        }
    }
}