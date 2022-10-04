package com.civilcam.domainLayer.usecase.subscriptions

import com.civilcam.domainLayer.repos.SubscriptionsRepository

class SetUserSubscriptionUseCase(
	private val subscriptionsRepository: SubscriptionsRepository
) {
	suspend fun invoke(receipt: String, productId: String) =
		subscriptionsRepository.setGoogleSubscription(receipt, productId)
}