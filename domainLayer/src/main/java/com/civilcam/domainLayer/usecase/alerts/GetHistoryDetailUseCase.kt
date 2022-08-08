package com.civilcam.domainLayer.usecase.alerts

import com.civilcam.domainLayer.repos.MockRepository

class GetHistoryDetailUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun getAlerts() = mockRepository.getAlerts()
}