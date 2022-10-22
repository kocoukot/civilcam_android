package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository

class ToggleSettingsUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(type: String, isOn: Boolean) =
        userRepository.toggleSettings(type, isOn)
}