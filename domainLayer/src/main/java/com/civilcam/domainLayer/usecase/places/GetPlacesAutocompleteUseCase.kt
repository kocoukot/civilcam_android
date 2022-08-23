package com.civilcam.domainLayer.usecase.places

import com.civilcam.domainLayer.repos.PlacesRepository

class GetPlacesAutocompleteUseCase(
    private val placesRepository: PlacesRepository
) {
    suspend fun invoke(query: String) = placesRepository.getPlacesWithType(query)
}