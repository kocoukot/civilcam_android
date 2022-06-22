package com.civilcam.domain.usecase.alerts

import com.civilcam.data.repository.MockRepository
import com.civilcam.domain.model.alerts.AlertType

class GetHistoryAlertListUseCase(
    private val mockRepository: MockRepository
) {
    suspend fun getAlerts(alertType: AlertType) = mockRepository.getHistoryAlert(alertType)
}