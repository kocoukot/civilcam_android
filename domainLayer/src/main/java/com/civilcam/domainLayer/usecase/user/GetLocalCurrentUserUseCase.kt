package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.AccountRepository

class GetLocalCurrentUserUseCase(
    private val accountRepository: AccountRepository
) {
    operator fun invoke() = accountRepository.getUser()
}