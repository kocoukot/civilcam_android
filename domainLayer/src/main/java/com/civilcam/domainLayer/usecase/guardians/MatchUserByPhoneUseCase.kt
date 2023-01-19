package com.civilcam.domainLayer.usecase.guardians

import com.civilcam.domainLayer.repos.GuardiansRepository

class MatchUserByPhoneUseCase(
    private val guardiansRepository: GuardiansRepository
) {
    suspend operator fun invoke(phoneList: List<String>) =
        guardiansRepository.matchByPhone(phoneList)
}