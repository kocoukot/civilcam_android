package com.civilcam.data.repository

import com.civilcam.domain.model.AutocompletePlace

interface PlacesRepository {

    suspend fun getPlacesWithType(
        query: String,
    ): List<AutocompletePlace>

//    fun getPlaceDetails(
//        placeId: String
//    ): Single<PlaceDetails>
}