package com.civilcam.domainLayer.usecase.guardians

import com.civilcam.domainLayer.repos.GuardiansRepository

class GetNetworkRequestsUseCase(
    private val guardiansRepository: GuardiansRepository
) {
    suspend operator fun invoke() = guardiansRepository.getUserRequest()
}