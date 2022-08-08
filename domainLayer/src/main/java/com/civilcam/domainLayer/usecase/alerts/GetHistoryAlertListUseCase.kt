package com.civilcam.domainLayer.usecase.alerts

import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.domainLayer.repos.MockRepository

class GetHistoryAlertListUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun getAlerts(alertType: AlertType) = mockRepository.getHistoryAlert(alertType)
}