package com.civilcam.domain.usecase.settings

import com.civilcam.data.repository.MockRepository

class CheckCurrentPasswordUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun checkPassword(currentPassword: String) =
        mockRepository.checkPassword(currentPassword)
}