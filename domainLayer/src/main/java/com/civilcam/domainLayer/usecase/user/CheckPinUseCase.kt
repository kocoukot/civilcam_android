package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository

class CheckPinUseCase(
	private val userRepository: UserRepository
) {
	suspend fun checkPin(pinCode: String) = userRepository.checkPin(pinCode)
}