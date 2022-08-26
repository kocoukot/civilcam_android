package com.civilcam.domainLayer.usecase.subscriptions

import com.civilcam.domainLayer.repos.SubscriptionsRepository

class GetSubscriptionsUseCase(
	private val subscriptionsRepository: SubscriptionsRepository
) {
	suspend fun getSubscriptions() = subscriptionsRepository.getSubscriptionsList()
}