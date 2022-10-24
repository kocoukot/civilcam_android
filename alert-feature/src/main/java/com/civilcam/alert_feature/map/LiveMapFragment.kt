package com.civilcam.alert_feature.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.alert_feature.map.model.LiveMapRoute
import com.civilcam.ext_features.ext.callPhone
import com.civilcam.ext_features.ext.navController
import com.civilcam.ext_features.ext.phoneNumberFormat
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.requireArg
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class LiveMapFragment : Fragment() {
    private val viewModel: LiveMapViewModel by viewModel {
        parametersOf(userId)
    }

    private val userId by requireArg<Int>(ARG_ALERT_USER_ID)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.tag("alert notif ID").i("userId $userId")
        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                LiveMapRoute.CloseAlert, LiveMapRoute.GoBack -> navController.popBackStack()
                is LiveMapRoute.CallUserPhone -> callPhone(route.userPhoneNumber.phoneNumberFormat())
                LiveMapRoute.CallPolice -> callPhone("911")
                LiveMapRoute.AlertResolved -> navController.popBackStack()
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

    override fun onResume() {
        super.onResume()
        viewModel.addListeners()

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeSocketListeners()
    }


    companion object {
        const val ARG_ALERT_USER_ID = "alert_user_id"

        fun createArgs(userId: Int) = bundleOf(
            ARG_ALERT_USER_ID to userId
        )
    }
}