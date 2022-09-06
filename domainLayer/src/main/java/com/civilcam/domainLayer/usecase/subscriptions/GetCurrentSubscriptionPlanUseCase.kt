package com.civilcam.domainLayer.usecase.subscriptions

import com.civilcam.domainLayer.repos.MockRepository

class GetCurrentSubscriptionPlanUseCase(
	private val mockRepository: MockRepository
) {
	suspend fun getCurrentSubscriptionPlan() =
		mockRepository.getCurrentSubscriptionPlan()
}