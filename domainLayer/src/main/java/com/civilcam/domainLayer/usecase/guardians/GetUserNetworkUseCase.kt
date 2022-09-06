package com.civilcam.domainLayer.usecase.guardians

import com.civilcam.domainLayer.repos.GuardiansRepository

class GetUserNetworkUseCase(
    private val guardiansRepository: GuardiansRepository
) {
    suspend operator fun invoke() = guardiansRepository.getUserNetwork()
}