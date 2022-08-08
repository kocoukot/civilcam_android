package com.civilcam.domainLayer.usecase.guardians

import com.civilcam.domainLayer.repos.MockRepository

class GetGuardsRequestsUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun getGuardRequests() = mockRepository.getGuardRequests()
}