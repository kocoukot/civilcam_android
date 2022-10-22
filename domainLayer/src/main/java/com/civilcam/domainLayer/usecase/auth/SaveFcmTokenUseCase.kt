package com.civilcam.domainLayer.usecase.auth

import com.civilcam.domainLayer.repos.AuthRepository


class SaveFcmTokenUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(token: String) = authRepository.setFcmToken(token)
}