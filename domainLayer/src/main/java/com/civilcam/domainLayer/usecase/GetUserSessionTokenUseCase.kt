package com.civilcam.domainLayer.usecase

import com.civilcam.domainLayer.repos.AccountRepository

class GetUserSessionTokenUseCase(
    private val accountRepository: AccountRepository,
) {
    operator fun invoke(): String = accountRepository.sessionToken
}