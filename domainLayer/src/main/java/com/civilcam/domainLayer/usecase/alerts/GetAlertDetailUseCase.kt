package com.civilcam.domainLayer.usecase.alerts

import com.civilcam.domainLayer.repos.AlertsRepository

class GetAlertDetailUseCase(
    private val alertsRepository: AlertsRepository
) {
    suspend operator fun invoke(alertId: Int) = alertsRepository.getAlertDetail(alertId)
}