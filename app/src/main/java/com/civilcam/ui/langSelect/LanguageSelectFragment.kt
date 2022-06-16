package com.civilcam.ui.langSelect

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.langSelect.model.LangSelectRoute
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LanguageSelectFragment : Fragment() {
    private val viewModel: LanguageSelectViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            Timber.d("decorView ${decorView.systemUiVisibility}")
            decorView.systemUiVisibility = 8192
            statusBarColor = Color.WHITE
//            statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        }

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