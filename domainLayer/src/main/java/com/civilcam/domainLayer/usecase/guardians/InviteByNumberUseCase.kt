package com.civilcam.domainLayer.usecase.guardians

import com.civilcam.domainLayer.repos.GuardiansRepository

class InviteByNumberUseCase(
    private val guardiansRepository: GuardiansRepository
) {
    suspend operator fun invoke(phoneNumber: String) =
        guardiansRepository.inviteByNumber(phoneNumber)
}