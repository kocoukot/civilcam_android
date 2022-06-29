package com.civilcam.ui.langSelect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.ui.MainActivity
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.langSelect.model.LangSelectRoute
import org.koin.androidx.viewmodel.ext.android.viewModel

class LanguageSelectFragment : Fragment() {
    private val viewModel: LanguageSelectViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).showBottomNavBar(false)
        WindowCompat.setDecorFitsSystemWindows(activity!!.window!!, true)

        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                LangSelectRoute.ToOnBoarding -> navController.navigate(R.id.action_languageSelectFragment_to_onBoardingFragment)
            }
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    viewLifecycleOwner
                )
            )

            setContent {
                LanguageSelectScreenContent(viewModel)
            }
        }
    }
}