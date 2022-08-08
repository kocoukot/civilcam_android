package com.civilcam.domainLayer.usecase.guardians

import com.civilcam.domainLayer.model.guard.NetworkType
import com.civilcam.domainLayer.repos.MockRepository

class GetGuardsListUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun getGuards(guardType: NetworkType) = mockRepository.getGuards(guardType)
}