package com.civilcam.domainLayer.usecase.alerts

import com.civilcam.domainLayer.repos.AlertsRepository

class GetHistoryAlertListUseCase(
    private val alertsRepository: AlertsRepository
) {
    suspend operator fun invoke(historyType: String) =
        alertsRepository.getAlertsHistory(historyType)
}