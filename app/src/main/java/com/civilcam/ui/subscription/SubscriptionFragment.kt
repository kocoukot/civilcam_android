package com.civilcam.ui.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.android.billingclient.api.ProductDetails
import com.civilcam.R
import com.civilcam.common.ext.navigateToRoot
import com.civilcam.common.ext.showAlertDialogFragment
import com.civilcam.domainLayer.model.subscription.UserSubscriptionState
import com.civilcam.ext_features.ext.hideSystemUI
import com.civilcam.ext_features.ext.navigateToStart
import com.civilcam.ext_features.ext.showSystemUI
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ext_features.requireArg
import com.civilcam.ui.auth.pincode.PinCodeFragment
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.subscription.model.BillingClientService
import com.civilcam.ui.subscription.model.BillingEvent
import com.civilcam.ui.subscription.model.SubscriptionActions
import com.civilcam.ui.subscription.model.SubscriptionRoute
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SubscriptionFragment : Fragment(), BillingEvent {
	private val viewModel: SubscriptionViewModel by viewModel {
		parametersOf(useSubState)
	}

	private val useSubState: UserSubscriptionState by requireArg(ARG_FLOW)
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		BillingClientService.apply {
			init(requireContext(), requireActivity(), this@SubscriptionFragment)
			initializeBillingClient()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				SubscriptionRoute.GoBack -> navController.popBackStack()
				SubscriptionRoute.GoProfileSetup -> navController.navigate(R.id.profileSetupFragment)
				SubscriptionRoute.GoMap -> navController.navigateToRoot(R.id.emergency_root)
				SubscriptionRoute.GoCreateAccount -> navigateToStart()
				SubscriptionRoute.GoPinCode -> {
					navController.popBackStack()
					navController.navigate(
						R.id.pinCodeFragment,
						PinCodeFragment.createArgs(PinCodeFlow.CREATE_PIN_CODE, true)
					)
				}
				is SubscriptionRoute.LaunchPurchase -> launchPurchase(route.purchase)
			}
		}
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(
				ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
					viewLifecycleOwner
				)
			)
			setContent {
				SubscriptionScreenContent(viewModel)
			}
		}
	}
	
	private fun launchPurchase(purchase: ProductDetails) {
		BillingClientService.launchPurchaseFlow(purchase)
	}
	
	override fun onResume() {
		super.onResume()
		hideSystemUI()
		BillingClientService.handlePurchaseVerification()
	}

	override fun onStop() {
		super.onStop()
		showSystemUI()
	}
	
	override fun onProductListReady(list: List<ProductDetails>) {
		viewModel.setInputActions(SubscriptionActions.SetProductList(list))
	}
	
	override fun setPurchaseToken(token: String) {
		viewModel.setInputActions(SubscriptionActions.SetPurchaseToken(token))
	}
	
	override fun onConnectionError(message: String) {
		showAlertDialogFragment(message, "") {
			navController.popBackStack()
		}
	}
	
	companion object {
		private const val ARG_FLOW = "subscription_flow"
		const val IN_APP_TRIAL = "in_app_trial"
		
		fun createArgs(isReselect: UserSubscriptionState) = bundleOf(
			ARG_FLOW to isReselect
		)
	}
}