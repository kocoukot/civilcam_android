package com.civilcam.ui.profile.userProfile

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.civilcam.BuildConfig
import com.civilcam.R
import com.civilcam.common.ext.navigateToRoot
import com.civilcam.common.ext.showAlertDialogFragment
import com.civilcam.ui.auth.pincode.PinCodeFragment
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.registerForPermissionsResult
import com.civilcam.ui.profile.credentials.ChangeCredentialsFragment
import com.civilcam.ui.profile.userProfile.model.UserProfileRoute
import com.civilcam.ui.verification.VerificationFragment
import com.civilcam.utils.contract.GalleryActivityResultContract
import com.google.android.libraries.places.api.Places
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class UserProfileFragment : Fragment() {
	private val viewModel: UserProfileViewModel by viewModel()


	private val cameraPermissionsDelegate =
		if (Build.VERSION.SDK_INT >= 33)
			registerForPermissionsResult(Manifest.permission.READ_MEDIA_IMAGES)
			{ onPermissionsGranted(it) }
		else {
			registerForPermissionsResult(Manifest.permission.READ_EXTERNAL_STORAGE)
			{ onPermissionsGranted(it) }
		}

	private val chooseFromGalleryActivityLauncher =
		registerForActivityResult(GalleryActivityResultContract()) { uri ->
			Timber.d("onPictureUriReceived $uri")
			uri?.let(viewModel::onPictureUriReceived)
		}

	private var pendingAction: (() -> Unit)? = null

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		setFragmentResultListener(VerificationFragment.RESULT_BACK_STACK) { _, _ ->
			viewModel.fetchCurrentUser()
		}

		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				UserProfileRoute.ForceLogout -> navController.navigateToRoot(R.id.onBoardingFragment)
				UserProfileRoute.GoBack -> navController.popBackStack()
				is UserProfileRoute.GoCredentials -> {
					navController.navigate(
						R.id.changeCredentialsFragment,
						ChangeCredentialsFragment.createArgs(
							route.userProfileType,
							route.credential.orEmpty()
						)
					)
				}
				UserProfileRoute.GoGalleryOpen -> onChooseFromGalleryCaseClicked()
				UserProfileRoute.GoPinCode -> navController.navigate(
					R.id.pinCodeFragment,
					PinCodeFragment.createArgs(PinCodeFlow.CURRENT_PIN_CODE)
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
				UserProfileScreenContent(viewModel)
			}
		}
    }

    private fun onChooseFromGalleryCaseClicked() {
        if (cameraPermissionsDelegate.checkSelfPermissions()) {
            chooseFromGalleryActivityLauncher.launch(Unit)
        } else {
            pendingAction = { onChooseFromGalleryCaseClicked() }
            cameraPermissionsDelegate.requestPermissions()
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

    private fun onPermissionsGranted(isGranted: Boolean) {
        if (isGranted) {
            pendingAction?.invoke()
            pendingAction = null
        } else {
            showAlertDialogFragment(title = "Sorry, we need permission!")
        }
    }
}