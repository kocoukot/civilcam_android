package com.civilcam.domainLayer.usecase

import com.civilcam.domainLayer.repos.MockRepository

class GetUserInformationUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun getUser(userId: String) = mockRepository.getUserInformation(userId)
}