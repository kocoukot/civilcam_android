package com.civilcam.ui.subscription.model

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.android.billingclient.api.*
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class BillingClientService(
	private val mContext: Context,
	private val mActivity: Activity,
	private val billingEvent: BillingEvent
) {
	
	private lateinit var billingClient: BillingClient
	private lateinit var userId: String
	
	fun initializeBillingClient(id: String) {
		userId = id
		billingClient = BillingClient.newBuilder(mContext).setListener(purchasesUpdatedListener)
			.enablePendingPurchases().build()
		establishConnection()
	}
	
	fun launchPurchaseFlow(productDetails: ProductDetails) {
		val productDetailsParamsList =
			listOf(productDetails.subscriptionOfferDetails?.get(0)?.offerToken?.let {
				BillingFlowParams.ProductDetailsParams.newBuilder()
					.setProductDetails(productDetails).setOfferToken(it).build()
			})
		Timber.i("USER_ID: $userId")
		val billingFlowParams =
			BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList)
				.setObfuscatedAccountId(userId)
				.build()
		billingClient.launchBillingFlow(mActivity, billingFlowParams)
	}
	
	fun handlePurchaseVerification() {
		val params =
			QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS)
		runBlocking {
			billingClient.queryPurchasesAsync(
				params.build()
			) { billingResult, list ->
				when (billingResult.responseCode) {
					BillingClient.BillingResponseCode.OK -> {
						for (purchase in list) {
							if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
								verifySubPurchase(purchase)
							}
						}
					}
				}
			}
		}
	}
	
	private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
		if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
			for (purchase in purchases) {
				if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
					verifySubPurchase(purchase)
				}
				if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
					billingEvent.onConnectionError(billingResult.debugMessage)
				}
			}
		}
	}
	
	private fun establishConnection() {
		billingClient.startConnection(object : BillingClientStateListener {
			override fun onBillingSetupFinished(billingResult: BillingResult) {
				if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
					showProducts()
				} else {
					billingEvent.onConnectionError(billingResult.debugMessage)
				}
			}
			
			override fun onBillingServiceDisconnected() {
				establishConnection()
			}
		})
	}
	
	private fun verifySubPurchase(purchase: Purchase) {
		val acknowledgePurchaseParams =
			AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
		
		Handler(Looper.getMainLooper()).postDelayed({
			billingClient.acknowledgePurchase(
				acknowledgePurchaseParams
			) { billingResult ->
				if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
					billingEvent.setPurchaseToken(purchase.purchaseToken)
				}
			}
		}, 100)
	}
	
	private fun showProducts() {
		val queryProductDetailsParams = QueryProductDetailsParams.newBuilder().setProductList(
			ImmutableList.of(
				QueryProductDetailsParams.Product.newBuilder().setProductId(PRODUCT.PREMIUM_MONTHLY)
					.setProductType(BillingClient.ProductType.SUBS).build(),
				QueryProductDetailsParams.Product.newBuilder().setProductId(PRODUCT.PREMIUM_YEARLY)
					.setProductType(BillingClient.ProductType.SUBS).build()
			)
		).build()
		
		billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
			if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
				Handler(Looper.getMainLooper()).postDelayed({
					billingEvent.onProductListReady(productDetailsList)
				}, 0)
			}
		}
	}
	
	object PRODUCT {
		const val PREMIUM_MONTHLY = "monthly5"
		const val PREMIUM_YEARLY = "yearly50"
	}
}

interface BillingEvent {
	fun onProductListReady(list: List<ProductDetails>)
	fun setPurchaseToken(token: String)
	fun onConnectionError(message: String)
}