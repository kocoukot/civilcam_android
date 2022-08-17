package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.model.LanguageType
import com.civilcam.domainLayer.repos.UserRepository

class SetUserLanguageUseCase(
	private val userRepository: UserRepository
) {
	suspend operator fun invoke(languageType: LanguageType) =
        userRepository.setUserLanguage(languageType)
}