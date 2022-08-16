package com.civilcam.domainLayer.repos

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun fetchLastKnownLocation(): LatLng

    suspend fun fetchLocation(): Flow<Pair<LatLng, Float>>
}
