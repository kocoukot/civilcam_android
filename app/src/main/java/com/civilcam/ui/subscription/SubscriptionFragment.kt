package com.civilcam.ui.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.android.billingclient.api.*
import com.civilcam.R
import com.civilcam.common.ext.navigateToRoot
import com.civilcam.domainLayer.model.subscription.UserSubscriptionState
import com.civilcam.ext_features.ext.hideSystemUI
import com.civilcam.ext_features.ext.navigateToStart
import com.civilcam.ext_features.ext.showSystemUI
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ext_features.requireArg
import com.civilcam.ui.auth.pincode.PinCodeFragment
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.subscription.model.SubscriptionRoute
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class SubscriptionFragment : Fragment() {
	private val viewModel: SubscriptionViewModel by viewModel {
		parametersOf(useSubState)
	}

	private val useSubState: UserSubscriptionState by requireArg(ARG_FLOW)


	private val purchasesUpdatedListener =
		PurchasesUpdatedListener { billingResult, purchases ->
		}

	private val billingClient by lazy {
		BillingClient.newBuilder(requireContext())
			.setListener(purchasesUpdatedListener)
			.enablePendingPurchases()
			.build()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		billStartConnection()
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

	override fun onResume() {
		super.onResume()
		hideSystemUI()
	}

	override fun onStop() {
		super.onStop()
		showSystemUI()
	}

	private fun billStartConnection() {
		billingClient.startConnection(object : BillingClientStateListener {
			override fun onBillingSetupFinished(billingResult: BillingResult) {
				if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

				}
			}

			override fun onBillingServiceDisconnected() {
				billStartConnection()
			}
		})
	}

	private fun getProductsList() {
		val queryProductDetailsParams =
			QueryProductDetailsParams.newBuilder()
				.setProductList(
					listOf(
						QueryProductDetailsParams.Product.newBuilder()
//							.setProductId(ONE_TRY_PRODUCT_ID)
//							.setProductType(BillingClient.ProductType.INAPP)
							.build(),

						QueryProductDetailsParams.Product.newBuilder()
//							.setProductId(ONE_DAY_PRODUCT_ID)
//							.setProductType(BillingClient.ProductType.INAPP)
							.build()
					)
				)
				.build()

		billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
			if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
				Timber.i("productDetailsList $productDetailsList")
//				viewModel.onBillsGot(productDetailsList.toList())
			}
		}
	}

	private fun launchBillFlow(product: ProductDetails) {
		val productDetailsParamsList = listOf(
			BillingFlowParams.ProductDetailsParams.newBuilder()
				.setProductDetails(product)
				.build()
		)

		val billingFlowParams = BillingFlowParams.newBuilder()
			.setProductDetailsParamsList(productDetailsParamsList)
			.build()
		billingClient.launchBillingFlow(requireActivity(), billingFlowParams)
	}

	private fun handlePurchase(purchase: Purchase) {
		val consumeParams =
			ConsumeParams.newBuilder()
				.setPurchaseToken(purchase.purchaseToken)
				.build()
//		lifecycleScope.launch {
//			withContext(Dispatchers.IO) {
//				billingClient.consumePurchase(consumeParams)
//			}
//		}
	}

	companion object {
		private const val ARG_FLOW = "subscription_flow"
		const val IN_APP_TRIAL = "in_app_trial"

		fun createArgs(isReselect: UserSubscriptionState) = bundleOf(
			ARG_FLOW to isReselect
		)
	}
}