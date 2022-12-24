package com.civilcam.domainLayer.usecase.subscriptions

import com.civilcam.domainLayer.repos.SubscriptionsRepository

class GetUserSubscriptionUseCase(
	private val subscriptionsRepository: SubscriptionsRepository
) {
	suspend operator fun invoke() = subscriptionsRepository.getUserSubscription()
}