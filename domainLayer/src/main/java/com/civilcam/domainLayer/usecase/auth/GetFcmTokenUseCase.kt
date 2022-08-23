package com.civilcam.domainLayer.usecase.auth

import com.civilcam.domainLayer.repos.AuthRepository


class GetFcmTokenUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.getFcmToken()
}