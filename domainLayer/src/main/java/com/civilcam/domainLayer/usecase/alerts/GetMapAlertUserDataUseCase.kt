package com.civilcam.domainLayer.usecase.alerts

import com.civilcam.domainLayer.repos.MockRepository

class GetMapAlertUserDataUseCase(
    private val mockRepository: MockRepository
) {
    suspend operator fun invoke(alertId: Int) = mockRepository.getMapAlert()
}