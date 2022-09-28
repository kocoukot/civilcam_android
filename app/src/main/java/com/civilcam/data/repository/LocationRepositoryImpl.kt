package com.civilcam.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Looper
import com.civilcam.common.ext.awaitResult
import com.civilcam.domainLayer.repos.LocationRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import timber.log.Timber


class LocationRepositoryImpl(
    private val context: Context,
) : LocationRepository {
    private val locationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }



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
    override suspend fun fetchLocation(updateInterval: Long): Flow<Pair<LatLng, Float>> =
        callbackFlow {

            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGpsEnabled && !isNetworkEnabled) {
                throw Throwable("GPS is disabled")
            }


            val locationRequest = LocationRequest.create().apply {
                interval = updateInterval
                fastestInterval = updateInterval
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }


            val callBack = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    locationResult.locations.lastOrNull()?.let { location ->
                        launch {
                            send(LatLng(location.latitude, location.longitude) to location.bearing)
                        }
                    }
                }
        }

        locationClient.requestLocationUpdates(locationRequest, callBack, Looper.getMainLooper())
        awaitClose { locationClient.removeLocationUpdates(callBack) }
    }
}