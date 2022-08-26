package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository

class ContactSupportUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(issue: String, text: String, email: String) =
        userRepository.contactSupport(issue, text, email)
}