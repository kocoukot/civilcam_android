package com.civilcam.domain.usecase.guardians

import com.civilcam.data.repository.MockRepository
import com.civilcam.domain.model.guard.NetworkType

class GetGuardsListUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun getGuards(guardType: NetworkType) = mockRepository.getGuards(guardType)
}