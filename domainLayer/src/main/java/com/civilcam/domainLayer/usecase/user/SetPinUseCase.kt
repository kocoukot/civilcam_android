package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository

class SetPinUseCase(
	private val userRepository: UserRepository
) {
	suspend fun setPin(currentPinCode: String?, newPinCode: String) =
		userRepository.setPin(currentPinCode, newPinCode)
}