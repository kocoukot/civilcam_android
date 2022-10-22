package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.AutocompletePlace

interface PlacesRepository {

    suspend fun getPlacesWithType(
        query: String,
    ): List<AutocompletePlace>
}