package com.civilcam.ui.profile.userProfile

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.common.ext.showAlertDialogFragment
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.registerForPermissionsResult
import com.civilcam.ui.common.ext.requireArg
import com.civilcam.ui.profile.credentials.ChangeCredentialsFragment
import com.civilcam.ui.profile.credentials.model.CredentialType
import com.civilcam.ui.profile.userProfile.model.UserProfileRoute
import com.civilcam.ui.profile.userProfile.model.UserProfileType
import com.civilcam.utils.contract.GalleryActivityResultContract
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class UserProfileFragment : Fragment() {
	private val viewModel: UserProfileViewModel by viewModel()
	
	private val cameraPermissionsDelegate = registerForPermissionsResult(
		Manifest.permission.READ_EXTERNAL_STORAGE,
		Manifest.permission.WRITE_EXTERNAL_STORAGE,
	) { onPermissionsGranted(it) }
	
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
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				UserProfileRoute.GoBack -> navController.popBackStack()
				is UserProfileRoute.GoCredentials -> {
					when(route.userProfileType) {
						UserProfileType.PHONE_NUMBER -> {
							navController.navigate(
								R.id.changeCredentialsFragment,
								ChangeCredentialsFragment.createArgs(
									CredentialType.PHONE
								)
							)
						}
						UserProfileType.EMAIL -> {
							navController.navigate(
								R.id.changeCredentialsFragment,
								ChangeCredentialsFragment.createArgs(
									CredentialType.EMAIL
								)
							)
						}
						UserProfileType.PIN_CODE -> {}
					}
				}
				UserProfileRoute.GoGalleryOpen -> onChooseFromGalleryCaseClicked()
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
	
	private fun onPermissionsGranted(isGranted: Boolean) {
		if (isGranted) {
			pendingAction?.invoke()
			pendingAction = null
		} else {
			showAlertDialogFragment(title = "Sorry, we need permission!")
		}
	}
}