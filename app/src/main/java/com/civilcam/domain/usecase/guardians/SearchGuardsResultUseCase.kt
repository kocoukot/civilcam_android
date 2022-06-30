package com.civilcam.domain.usecase.guardians

import com.civilcam.data.repository.MockRepository

class SearchGuardsResultUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun searchGuards(searchQuery: String) = mockRepository.searchGuards(searchQuery)
}