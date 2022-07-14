package com.civilcam.domain.usecase.location

import com.civilcam.data.repository.PlacesRepository
import com.civilcam.domain.model.AutocompletePlace

class GetPlacesAutocompleteUseCase(
    private val placesRepository: PlacesRepository
) {

    suspend fun invoke(
        query: String,
        result: (List<AutocompletePlace>) -> Unit
    ) = placesRepository.getPlacesWithType(query) {
        result.invoke(it)
    }

}