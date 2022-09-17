package com.civilcam.domainLayer.usecase.alerts

import com.civilcam.domainLayer.repos.AlertsRepository
import com.google.android.gms.maps.model.LatLng

class SendEmergencySosUseCase(
    private val alertsRepository: AlertsRepository
) {
    suspend operator fun invoke(location: String, coords: LatLng) =
        alertsRepository.updateSosCoords(location, coords)
}