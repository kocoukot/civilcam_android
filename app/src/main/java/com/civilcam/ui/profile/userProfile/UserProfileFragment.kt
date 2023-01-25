package com.civilcam.ui.profile.userProfile

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.fragment.app.setFragmentResultListener
import com.civilcam.BuildConfig
import com.civilcam.R
import com.civilcam.ext_features.alert.AlertDialogButtons
import com.civilcam.ext_features.arch.BaseFragment
import com.civilcam.ui.auth.pincode.PinCodeFragment
import com.civilcam.ui.common.alert.DialogAlertFragment
import com.civilcam.ui.verification.VerificationFragment
import com.google.android.libraries.places.api.Places
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfileFragment : BaseFragment<UserProfileViewModel>() {


	override val viewModel: UserProfileViewModel by viewModel()

	override val screenContent: @Composable (UserProfileViewModel) -> Unit = {
		UserProfileScreenContent(viewModel)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setFragmentResultListener(VerificationFragment.RESULT_BACK_EMAIL) { _, _ ->
			showAlert(resources.getString(R.string.user_profile_email_change_desc))
		}

		setFragmentResultListener(VerificationFragment.RESULT_BACK_PHONE) { _, _ ->
			showAlert(resources.getString(R.string.user_profile_phone_change_desc))
		}

		setFragmentResultListener(PinCodeFragment.RESULT_SAVED_NEW_PIN) { key, bundle ->
			if (bundle.getBoolean(key)) {
				showAlert(resources.getString(com.civilcam.ext_features.R.string.pincode_changed_alert_text))
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

	private fun showAlert(text: String) {
		viewModel.fetchCurrentUser()
		DialogAlertFragment.create(
			parentFragmentManager,
			text,
			AlertDialogButtons.OK
		)
	}
}