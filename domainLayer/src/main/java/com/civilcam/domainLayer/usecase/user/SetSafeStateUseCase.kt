package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository
import com.google.android.gms.maps.model.LatLng

class SetSafeStateUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(pinCode: String, coords: LatLng) =
        userRepository.setSafeState(pinCode, coords)
}