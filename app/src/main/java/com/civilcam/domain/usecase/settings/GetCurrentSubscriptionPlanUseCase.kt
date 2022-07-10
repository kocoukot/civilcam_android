package com.civilcam.domain.usecase.settings

import com.civilcam.data.repository.MockRepository

class GetCurrentSubscriptionPlanUseCase(
	private val mockRepository: MockRepository
) {
	suspend fun getCurrentSubscriptionPlan() =
		mockRepository.getCurrentSubscriptionPlan()
}