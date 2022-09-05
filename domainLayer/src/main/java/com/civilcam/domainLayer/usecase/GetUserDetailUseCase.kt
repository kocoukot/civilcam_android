package com.civilcam.domainLayer.usecase

import com.civilcam.domainLayer.repos.GuardiansRepository

class GetUserDetailUseCase(
    private val guardiansRepository: GuardiansRepository
) {
    suspend fun getUser(userId: Int) = guardiansRepository.getPersonDetail(userId)
}