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
import com.civilcam.billing_service.BillingClientService
import com.civilcam.billing_service.BillingEvent
import com.civilcam.common.ext.navigateToRoot
import com.civilcam.common.ext.showAlertDialogFragment
import com.civilcam.domainLayer.model.subscription.UserSubscriptionState
import com.civilcam.domainLayer.usecase.user.GetLocalCurrentUserUseCase
import com.civilcam.ext_features.ext.hideSystemUI
import com.civilcam.ext_features.ext.navigateToStart
import com.civilcam.ext_features.ext.showSystemUI
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ext_features.requireArg
import com.civilcam.ui.auth.pincode.PinCodeFragment
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.subscription.model.SubscriptionActions
import com.civilcam.ui.subscription.model.SubscriptionRoute
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class SubscriptionFragment : Fragment(), BillingEvent {
	private val viewModel: SubscriptionViewModel by viewModel {
		parametersOf(useSubState)
	}
	private val getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase by inject()
	private val useSubState: UserSubscriptionState by requireArg(ARG_FLOW)
	
	private val billService by lazy {
		BillingClientService(this@SubscriptionFragment)
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		billService.initializeBillingClient(
			getLocalCurrentUserUseCase.invoke()?.sessionUser?.id.toString(),
			requireContext()
		)
	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
		billService.launchPurchaseFlow(purchase, requireActivity())
	}
	
	override fun onResume() {
		super.onResume()
		hideSystemUI()
		billService.handlePurchaseVerification()
	}
	
	override fun onStop() {
		super.onStop()
		showSystemUI()
	}
	
	override fun onProductListReady(list: List<ProductDetails>) {
		viewModel.setInputActions(SubscriptionActions.SetProductList(list))
		Timber.i("Product list: $list")
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