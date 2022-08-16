package com.civilcam.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.civilcam.common.ext.awaitResult
import com.civilcam.domainLayer.repos.LocationRepository
import com.civilcam.ui.common.Constant
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber


class LocationRepositoryImpl(
    private val context: Context,
) : LocationRepository {
    private val locationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val locationRequest = LocationRequest.create()
        .setInterval(Constant.LOCATION_REQUEST_INTERVAL_IN_MILLIS)
        .setFastestInterval(Constant.LOCATION_REQUEST_FASTEST_INTERVAL_IN_MILLIS)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)


    @SuppressLint("MissingPermission")
    override suspend fun fetchLastKnownLocation(): LatLng {
        Timber.i("fetchLastKnownLocation $locationClient")
        val gotLocation = locationClient.lastLocation.awaitResult()
        return gotLocation?.let { location ->
            LatLng(location.latitude, location.longitude)
        } ?: run {
            throw Throwable("Can't get your current location")
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun fetchLocation(): Flow<Pair<LatLng, Float>> = callbackFlow {
        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                trySend(LatLng(location.latitude, location.longitude) to location.bearing)
            }
        }

        locationClient.requestLocationUpdates(locationRequest, callBack, Looper.getMainLooper())
        awaitClose { locationClient.removeLocationUpdates(callBack) }
    }
}