package com.civilcam.langselect

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
import com.civilcam.ext_features.ext.navigateTo
import com.civilcam.ext_features.ext.registerForPermissionsResult
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.langselect.model.LangSelectRoute
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
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window!!, true)

        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                LangSelectRoute.ToOnBoarding -> navigateTo(getString(com.civilcam.ext_features.R.string.direction_onBoardingFragment))
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