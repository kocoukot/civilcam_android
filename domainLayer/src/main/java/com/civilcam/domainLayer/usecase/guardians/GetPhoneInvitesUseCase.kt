package com.civilcam.domainLayer.usecase.guardians

import com.civilcam.domainLayer.repos.GuardiansRepository

class GetPhoneInvitesUseCase(
    private val guardiansRepository: GuardiansRepository
) {
    suspend operator fun invoke() = guardiansRepository.getInvitesList()
}