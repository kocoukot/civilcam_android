package com.civilcam.domainLayer.usecase.guardians

import com.civilcam.domainLayer.repos.GuardiansRepository

class StopGuardingUseCase(
    private val guardiansRepository: GuardiansRepository
) {
    suspend operator fun invoke(personId: Int) = guardiansRepository.stopGuarding(personId)
}