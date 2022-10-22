package com.civilcam.ui.auth.create

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.common.ext.navigateByDirection
import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.ext_features.ext.showToast
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ui.auth.FacebookFragmentAuthHandler
import com.civilcam.ui.auth.GoogleFragmentAuthHandler
import com.civilcam.ui.auth.create.model.CreateAccountRoute
import com.civilcam.ui.common.NavigationDirection
import com.civilcam.ui.verification.VerificationFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CreateAccountFragment : Fragment() {
	private val viewModel: CreateAccountViewModel by viewModel()

	private val facebookAuthHandler =
		FacebookFragmentAuthHandler({
			if (it.isNotEmpty()) {
				viewModel.onFacebookSignedIn(it)
			} else {
				showToast("Unfortunately we couldn't get your profile information")
			}
		}, Timber::e)
	private val googleAuthHandler =
		GoogleFragmentAuthHandler(this, {
			if (it.isNotEmpty()) {
				viewModel.onGoogleSignedIn(it)
			} else {
				showToast("Unfortunately we couldn't get your profile information")
			}
		}, Timber::e)

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				CreateAccountRoute.GoBack -> {
					navController.popBackStack()
				}
				CreateAccountRoute.GoLogin -> {
					navController.navigate(R.id.loginFragment)
				}
				is CreateAccountRoute.GoContinue -> {
					navController.navigate(
						R.id.verificationFragment,
						VerificationFragment.createArgs(
							VerificationFlow.CURRENT_EMAIL,
							route.email,
							""
						)
					)
				}
				is CreateAccountRoute.GoSocialsLogin -> navController.navigateByDirection(
					NavigationDirection.resolveDirectionFor(route.user)
				)
				CreateAccountRoute.OnFacebookSignIn -> facebookAuthHandler.auth(this)
				CreateAccountRoute.OnGoogleSignIn -> googleAuthHandler.auth(this)
			}
		}
		
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(
				ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
					viewLifecycleOwner
				)
			)

			setContent {
				CreateAccountScreenContent(viewModel)
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		facebookAuthHandler.onActivityResult(requestCode, resultCode, data)
	}
}
