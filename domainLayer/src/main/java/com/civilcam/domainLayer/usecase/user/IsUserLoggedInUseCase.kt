package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.AccountRepository

class IsUserLoggedInUseCase(
    private val accountRepository: AccountRepository
) {
    operator fun invoke() = accountRepository.isUserLoggedIn
}