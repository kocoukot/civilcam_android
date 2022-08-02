package com.civilcam.ui.alerts.map

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
import com.civilcam.ui.alerts.map.model.LiveMapRoute
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class LiveMapFragment : Fragment() {
    private val viewModel: LiveMapViewModel by viewModel {
        parametersOf(userId)
    }

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

    companion object {
        const val ARG_ALERT_USER_ID = "alert_user_id"

        fun createArgs(userId: Int) = bundleOf(
            ARG_ALERT_USER_ID to userId
        )

    }
}