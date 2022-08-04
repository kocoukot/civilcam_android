package com.civilcam.domain.usecase.location

import com.civilcam.data.repository.PlacesRepository

class GetPlacesAutocompleteUseCase(
    private val placesRepository: PlacesRepository
) {

    suspend fun invoke(
        query: String,
    ) = placesRepository.getPlacesWithType(query)

}