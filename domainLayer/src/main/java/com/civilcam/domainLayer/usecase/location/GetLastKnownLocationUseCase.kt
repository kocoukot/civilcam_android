package com.civilcam.domainLayer.usecase.location

import com.civilcam.domainLayer.repos.LocationRepository


class GetLastKnownLocationUseCase(
    private val locationRepository: LocationRepository
) {

    suspend operator fun invoke() = locationRepository.fetchLastKnownLocation()
}
