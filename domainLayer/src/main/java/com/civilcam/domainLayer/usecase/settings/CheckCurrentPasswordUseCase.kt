package com.civilcam.domainLayer.usecase.settings

import com.civilcam.domainLayer.repos.MockRepository

class CheckCurrentPasswordUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun checkPassword(currentPassword: String) =
        mockRepository.checkPassword(currentPassword)
}