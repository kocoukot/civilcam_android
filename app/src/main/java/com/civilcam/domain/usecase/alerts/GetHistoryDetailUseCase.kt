package com.civilcam.domain.usecase.alerts

import com.civilcam.data.repository.MockRepository

class GetHistoryDetailUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun getAlerts() = mockRepository.getAlerts()
}