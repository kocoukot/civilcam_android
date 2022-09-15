package com.civilcam.data.network.service

import com.civilcam.data.network.Endpoint
import com.civilcam.data.network.model.request.alert.AlertDetailRequest
import com.civilcam.data.network.model.request.alert.AlertHistoryRequest
import com.civilcam.data.network.model.request.alert.AlertResolveRequest
import com.civilcam.data.network.model.request.alert.SosCoordsRequest
import com.civilcam.data.network.model.response.SuccessResponse
import com.civilcam.data.network.model.response.alert.AlertsDetailResponse
import com.civilcam.data.network.model.response.alert.AlertsListResponse
import com.civilcam.domainLayer.model.PaginationRequest
import retrofit2.http.Body
import retrofit2.http.POST


interface AlertService {

    @POST(Endpoint.Alerts.SOS_COORDINATES)
    suspend fun postSosCoordinates(@Body request: SosCoordsRequest): SuccessResponse

    @POST(Endpoint.Alerts.ALERTS_LIST)
    suspend fun getAlertsList(@Body request: PaginationRequest): AlertsListResponse

    @POST(Endpoint.Alerts.ALERTS_HISTORY)
    suspend fun getAlertsHistory(@Body request: AlertHistoryRequest): AlertsListResponse

    @POST(Endpoint.Alerts.ALERT_DETAILS)
    suspend fun getAlertDetails(@Body request: AlertDetailRequest): AlertsDetailResponse

    @POST(Endpoint.Alerts.ALERT_RESOLVE)
    suspend fun resolveAlert(@Body request: AlertResolveRequest): SuccessResponse
}











