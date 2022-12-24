package com.civilcam.domainLayer.usecase.alerts

import com.civilcam.domainLayer.repos.AlertsRepository

class GetStreamKeyUseCase(
    private val alertsRepository: AlertsRepository
) {
    operator fun invoke(): String = alertsRepository.getStreamKey()

}
