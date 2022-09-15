package com.civilcam.domainLayer.usecase.alerts

import com.civilcam.domainLayer.repos.AlertsRepository


class GetAlertsListUseCase(
    private val alertsRepository: AlertsRepository
) {
//    suspend operator fun invoke() = alertsRepository.getAlertsList()
}