package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository
import com.google.android.gms.maps.model.LatLng

class SetUserCoordsUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(coords: LatLng) = userRepository.setUserCoords(coords)
}