package com.civilcam.domainLayer.usecase.guardians

import com.civilcam.domainLayer.repos.GuardiansRepository

class DeleteGuardianUseCase(
    private val guardiansRepository: GuardiansRepository
) {
    suspend operator fun invoke(personId: Int) = guardiansRepository.askToGuard(personId)
}