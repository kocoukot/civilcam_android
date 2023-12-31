package com.civilcam.data.repository

import com.civilcam.common.ext.awaitResult
import com.civilcam.domainLayer.model.AutocompletePlace
import com.civilcam.domainLayer.repos.PlacesRepository
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import timber.log.Timber

class PlacesRepositoryImpl(private val placesClient: PlacesClient) : PlacesRepository {
    private val sessionToken by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        AutocompleteSessionToken.newInstance()
    }

    override suspend fun getPlacesWithType(query: String): List<AutocompletePlace> {
        val request = buildPlacesRequest(query)
        val predictResponse = placesClient.findAutocompletePredictions(request).awaitResult()
        return predictResponse?.let { response: FindAutocompletePredictionsResponse ->
            response.autocompletePredictions
                .map {
                    Timber.d("AutocompletePlace $it")
                    AutocompletePlace(
                        it.placeId,
                        it.getPrimaryText(null).toString(),
                        it.getSecondaryText(null).toString(),
                        it.getFullText(null).toString()
                    )
                }
        } ?: run {
            emptyList()
        }
    }

//    override fun getPlaceDetails(placeId: String): Single<PlaceDetails> = Single
//        .fromCallable { buildPlaceDetailsRequest(placeId) }
//        .subscribeOn(Schedulers.io())
//        .flatMap { request ->
//            Single.create { source ->
//                placesClient.fetchPlace(request)
//                    .addOnSuccessListener { response: FetchPlaceResponse ->
//                        source.onSuccess(
//                            with(response.place) {
//                                val components = addressComponents?.asList().orEmpty()
//                                var details = PlaceDetails(location = latLng)
//                                Timber.d("detailedFullAddress ${response.place}")
//
//                                components.forEach { component ->
//                                    when {
//                                        component.types.contains(STREET_NUMBER_VALUE_TYPE) ->
//                                            details = details.copy(street = component.name)
//                                        component.types.contains(ROUTE_VALUE_TYPE) ->
//                                            details =
//                                                details.copy(street = "${details.street} ${component.name}")
//                                        component.types.contains(CITY_VALUE_TYPE) ->
//                                            details = details.copy(city = component.name)
//                                        component.types.contains(STATE_VALUE_TYPE) ->
//                                            details = details.copy(
//                                                stateCode = component.shortName ?: component.name
//                                            )
//                                        component.types.contains(POSTAL_CODE_TYPE) ->
//                                            details = details.copy(zipCode = component.name)
//                                        component.types.contains(COUNTRY_VALUE_TYPE) ->
//                                            details = details.copy(
//                                                countryCode = component.shortName ?: component.name,
//                                            )
//                                    }
//                                }
//                                return@with details
//                            }
//                        )
//                    }
//                    .addOnFailureListener { source.onError(it) }
//            }
//        }


    private fun buildPlacesRequest(
        query: String,
    ): FindAutocompletePredictionsRequest {
        return FindAutocompletePredictionsRequest.builder()
            .setCountries("US")
            .setTypeFilter(TypeFilter.ADDRESS)
            .setSessionToken(sessionToken)
            .setQuery(query)
            .build()
    }

    private fun buildPlaceDetailsRequest(
        placeId: String
    ) = FetchPlaceRequest.newInstance(
        placeId,
        listOf(
            Place.Field.NAME,
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LAT_LNG
        )
    )

    companion object {
        private const val STREET_NUMBER_VALUE_TYPE = "street_number"
        private const val COUNTRY_VALUE_TYPE = "country"
        private const val ROUTE_VALUE_TYPE = "route"
        private const val CITY_VALUE_TYPE = "locality"
        private const val STATE_VALUE_TYPE = "administrative_area_level_1"
        private const val POSTAL_CODE_TYPE = "postal_code"
        private const val SUB_CITY_TYPE = "sublocality"
    }
}