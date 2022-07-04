package com.civilcam.ui.profile.setup

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.common.ext.showAlertDialogFragment
import com.civilcam.domain.model.VerificationFlow
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.registerForPermissionsResult
import com.civilcam.ui.profile.setup.model.ProfileSetupRoute
import com.civilcam.ui.verification.VerificationFragment
import com.civilcam.utils.contract.GalleryActivityResultContract
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ProfileSetupFragment : Fragment() {
	private val viewModel: ProfileSetupViewModel by viewModel()
	
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
				ProfileSetupRoute.GoBack -> navController.popBackStack()
				is ProfileSetupRoute.GoVerification -> navController.navigate(
					R.id.verificationFragment,
					VerificationFragment.createArgs(VerificationFlow.PHONE, resources.getString(R.string.verification_phone_mask, route.phoneNumber))
				)
				ProfileSetupRoute.GoLocationSelect -> {}
				ProfileSetupRoute.GoGalleryOpen -> onChooseFromGalleryCaseClicked()
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