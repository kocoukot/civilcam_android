package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.PaginationRequest
import com.civilcam.domainLayer.model.alerts.AlertDetailModel
import com.civilcam.domainLayer.model.alerts.AlertInfoModel
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.google.android.gms.maps.model.LatLng


interface AlertsRepository {

    suspend fun setSosCoords(location: String, coords: LatLng): AlertInfoModel

    suspend fun getAlertsList(page: PaginationRequest.Pagination): List<AlertModel>

    suspend fun getAlertsHistory(
        historyType: String,
        page: PaginationRequest.Pagination
    ): List<AlertModel>

    suspend fun getAlertDetail(alertId: Int): AlertDetailModel

    suspend fun resolveAlert(alertId: Int): Boolean


}