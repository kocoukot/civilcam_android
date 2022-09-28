package com.digi_crony.domain.usecase

import com.digi_crony.data.repository.AccountRepository

class GetUserSessionTokenUseCase(
    private val accountRepository: AccountRepository,
) {
    operator fun invoke(): String = accountRepository.sessionToken
}