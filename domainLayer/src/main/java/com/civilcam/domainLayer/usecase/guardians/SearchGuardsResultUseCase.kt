package com.civilcam.domainLayer.usecase.guardians

import com.civilcam.domainLayer.repos.MockRepository

class SearchGuardsResultUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun searchGuards(searchQuery: String) = mockRepository.searchGuards(searchQuery)
}