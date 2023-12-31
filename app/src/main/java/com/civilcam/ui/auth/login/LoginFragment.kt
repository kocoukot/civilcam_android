package com.civilcam.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.common.ext.navigateByDirection
import com.civilcam.ext_features.ext.showToast
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ui.auth.FacebookFragmentAuthHandler
import com.civilcam.ui.auth.GoogleFragmentAuthHandler
import com.civilcam.ui.auth.login.model.LoginRoute
import com.civilcam.ui.common.NavigationDirection
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LoginFragment : Fragment() {
	private val viewModel: LoginViewModel by viewModel()

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
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				is LoginRoute.GoLogin ->
					navController.navigateByDirection(
						NavigationDirection.resolveDirectionFor(route.user)
					)
				LoginRoute.GoRegister -> navController.navigate(R.id.createAccountFragment)
				LoginRoute.GoReset -> navController.navigate(R.id.resetPasswordFragment)
				LoginRoute.GoBack -> navController.popBackStack()
				LoginRoute.OnGoogleSignIn -> googleAuthHandler.auth(this)
				LoginRoute.OnFacebookSignIn -> facebookAuthHandler.auth(this)
			}
		}
		
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(
				ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
					viewLifecycleOwner
				)
			)
			setContent {
				LoginScreenContent(viewModel)
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		facebookAuthHandler.onActivityResult(requestCode, resultCode, data)
	}
}