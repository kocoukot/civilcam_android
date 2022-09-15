package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.alerts.AlertDetailModel
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.google.android.gms.maps.model.LatLng


interface AlertsRepository {

    suspend fun updateSosCoords(location: String, coords: LatLng): Boolean

    suspend fun getAlertsList(): List<AlertModel>

    suspend fun getAlertsHistory(historyType: String): List<AlertModel>

    suspend fun getAlertDetail(alertId: Int): AlertDetailModel

    suspend fun resolveAlert(alertId: Int): Boolean


}