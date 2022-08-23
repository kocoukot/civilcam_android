package com.civilcam.domainLayer.usecase.auth

import com.civilcam.domainLayer.repos.AuthRepository


class SetFcmTokenUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(token: String) = authRepository.setFcmToken(token)
}