package com.civilcam.domainLayer.usecase.location

import com.civilcam.domainLayer.repos.LocationRepository


class FetchUserLocationUseCase(
    private val locationRepository: LocationRepository
) {

    suspend operator fun invoke() = locationRepository.fetchLocation()
}
