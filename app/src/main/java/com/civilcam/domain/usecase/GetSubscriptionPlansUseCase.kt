package com.civilcam.domain.usecase

import com.civilcam.data.repository.MockRepository

class GetSubscriptionPlansUseCase(
	private val mockRepository: MockRepository
) {
	suspend fun getSubscriptionPlans() = mockRepository.getAvailableSubscriptionPlans()
}