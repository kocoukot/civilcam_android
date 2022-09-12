package com.civilcam.alert_feature.map

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.alert_feature.map.model.LiveMapRoute
import com.civilcam.ext_features.ext.navController
import com.civilcam.ext_features.ext.registerForPermissionsResult
import com.civilcam.ext_features.live_data.observeNonNull
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class LiveMapFragment : Fragment() {
    private val viewModel: LiveMapViewModel by viewModel {
        parametersOf(userId)
    }
    private val permissionsDelegate = registerForPermissionsResult(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    ) { onPermissionsGranted(it) }

    private var pendingAction: (() -> Unit)? = null
    private val userId = 1//by requireArg<Int>(ARG_ALERT_USER_ID)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                LiveMapRoute.GoBack -> navController.popBackStack()
                is LiveMapRoute.CallUserPhone -> callPhone(route.userPhoneNumber)
                LiveMapRoute.CallPolice -> callPhone("911")
                LiveMapRoute.AlertResolved -> navController.popBackStack()
                LiveMapRoute.CheckPermission -> checkPermissions()
            }
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    viewLifecycleOwner
                )
            )

            setContent {
                LiveMapScreenContent(viewModel)
            }
        }
    }

    private fun callPhone(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    private fun checkPermissions() {
        if (permissionsDelegate.checkSelfPermissions()) {
            viewModel.fetchUserLocation()
        } else {
            pendingAction = { checkPermissions() }
            Timber.i("checkPermission request ")
            permissionsDelegate.requestPermissions()
        }
        viewModel.isLocationAllowed(permissionsDelegate.checkSelfPermissions())
    }

    private fun onPermissionsGranted(isGranted: Boolean) {
        Timber.i("onPermissionsGranted $isGranted")
        pendingAction = if (isGranted) {
            pendingAction?.invoke()
            null
        } else {
            null
        }
    }


    override fun onStart() {
        super.onStart()
        checkPermissions()
    }


    companion object {
        const val ARG_ALERT_USER_ID = "alert_user_id"

        fun createArgs(userId: Int) = bundleOf(
            ARG_ALERT_USER_ID to userId
        )

    }
}