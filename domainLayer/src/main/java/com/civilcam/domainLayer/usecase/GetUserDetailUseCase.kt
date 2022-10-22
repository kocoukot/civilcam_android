package com.civilcam.domainLayer.usecase

import com.civilcam.domainLayer.repos.GuardiansRepository

class GetUserDetailUseCase(
    private val guardiansRepository: GuardiansRepository
) {
    suspend operator fun invoke(userId: Int) = guardiansRepository.getPersonDetail(userId)
}