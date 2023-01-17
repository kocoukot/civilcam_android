package com.civilcam.domainLayer.usecase.subscriptions

import com.civilcam.domainLayer.repos.SubscriptionsRepository

class SetTrialSubscriptionUseCase(
	private val subscriptionsRepository: SubscriptionsRepository
) {
	suspend operator fun invoke(productId: String) =
		subscriptionsRepository.setTrialSubscription(productId)
}