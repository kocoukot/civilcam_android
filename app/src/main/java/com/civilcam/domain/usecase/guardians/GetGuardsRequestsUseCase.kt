package com.civilcam.domain.usecase.guardians

import com.civilcam.data.repository.MockRepository

class GetGuardsRequestsUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun getGuardRequests() = mockRepository.getGuardRequests()
}