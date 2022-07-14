package com.civilcam.data.repository

import com.civilcam.domain.model.AutocompletePlace

interface PlacesRepository {

    suspend fun getPlacesWithType(
        query: String,
        result: (List<AutocompletePlace>) -> Unit
    )

//    fun getPlaceDetails(
//        placeId: String
//    ): Single<PlaceDetails>
}