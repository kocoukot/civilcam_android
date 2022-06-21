package com.civilcam.domain.usecase

import com.civilcam.data.repository.MockRepository

class GetUserInformationUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun getUser(userId: String) = mockRepository.getUserInformation(userId)
}