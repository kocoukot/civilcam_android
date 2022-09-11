package com.civilcam.ui.langSelect

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ui.MainActivity
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.registerForPermissionsResult
import com.civilcam.ui.langSelect.model.LangSelectRoute
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LanguageSelectFragment : Fragment() {
    private val viewModel: LanguageSelectViewModel by viewModel()

    @SuppressLint("InlinedApi")
    private val permissionsDelegate = registerForPermissionsResult(
        Manifest.permission.POST_NOTIFICATIONS,
    ) { onPermissionsGranted(it) }

    private var pendingAction: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).showBottomNavBar(false)
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window!!, true)

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

    private fun checkPermissions() {
        if (!permissionsDelegate.checkSelfPermissions()) {
            pendingAction = { checkPermissions() }
            permissionsDelegate.requestPermissions()
        }
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= 33) {
            checkPermissions()
        }
    }

    private fun onPermissionsGranted(isGranted: Boolean) {
        Timber.i("onPermissionsGranted $isGranted")
        if (isGranted) {
            pendingAction?.invoke()
            pendingAction = null
        }
    }
}