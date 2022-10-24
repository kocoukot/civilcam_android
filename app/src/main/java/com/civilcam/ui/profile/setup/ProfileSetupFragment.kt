package com.civilcam.ui.profile.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.BuildConfig
import com.civilcam.R
import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ui.profile.setup.model.ProfileSetupRoute
import com.civilcam.ui.verification.VerificationFragment
import com.google.android.libraries.places.api.Places
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileSetupFragment : Fragment() {
	private val viewModel: ProfileSetupViewModel by viewModel()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				ProfileSetupRoute.GoBack -> navController.popBackStack()
				is ProfileSetupRoute.GoVerification -> navController.navigate(
					R.id.verificationFragment,
					VerificationFragment.createArgs(
						VerificationFlow.NEW_PHONE,
						resources.getString(R.string.verification_phone_mask, route.phoneNumber),
						""
					)
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
                ProfileSetupScreenContent(viewModel)
            }
        }
    }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Places.initialize(requireContext(), BuildConfig.GOOGLE_AUTOCOMPLETE_KEY)
	}

	override fun onDestroy() {
		super.onDestroy()
		Places.deinitialize()
	}
}