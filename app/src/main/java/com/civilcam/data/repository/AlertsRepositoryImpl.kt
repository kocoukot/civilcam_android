package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage
import com.civilcam.data.mapper.alerts.AlertDetailMapper
import com.civilcam.data.mapper.alerts.AlertListMapper
import com.civilcam.data.network.model.request.alert.*
import com.civilcam.data.network.service.AlertService
import com.civilcam.data.network.support.BaseRepository
import com.civilcam.data.network.support.Resource
import com.civilcam.domainLayer.model.PaginationRequest
import com.civilcam.domainLayer.model.alerts.AlertDetailModel
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.repos.AlertsRepository
import com.google.android.gms.maps.model.LatLng


class AlertsRepositoryImpl(
    private val accountStorage: AccountStorage,
    private val alertService: AlertService,
) : AlertsRepository, BaseRepository() {

    private val alertListMapper = AlertListMapper()
    private val alertDetailMapper = AlertDetailMapper()

    override suspend fun updateSosCoords(location: String, coords: LatLng): Boolean =
        safeApiCall {
            alertService.postSosCoordinates(
                SosCoordsRequest(
                    location = location,
                    coords = CoordsRequest(
                        latitude = coords.latitude,
                        longitude = coords.longitude
                    ),
                )
            )
        }.let { response ->
            when (response) {
                is Resource.Success -> true
                is Resource.Failure -> throw response.serviceException
            }
        }

    override suspend fun getAlertsList(page: PaginationRequest.Pagination): List<AlertModel> =
        safeApiCall {
            alertService.getAlertsList(PaginationRequest(page))
        }.let { response ->
            when (response) {
                is Resource.Success -> response.value.list.map { alertListMapper.mapData(it) }
                is Resource.Failure -> throw response.serviceException
            }
        }

    override suspend fun getAlertsHistory(
        historyType: String,
        page: PaginationRequest.Pagination
    ): List<AlertModel> =
        safeApiCall {
            alertService.getAlertsHistory(
                AlertHistoryRequest(
                    pageInfo = page,
                    alertsType = historyType
                )
            )
        }.let { response ->
            when (response) {
                is Resource.Success -> response.value.list.map { alertListMapper.mapData(it) }
                is Resource.Failure -> throw response.serviceException
            }
        }

    override suspend fun getAlertDetail(alertId: Int): AlertDetailModel =
        safeApiCall {
            alertService.getAlertDetails(
                AlertDetailRequest(id = alertId)
            )
        }.let { response ->
            when (response) {
                is Resource.Success -> alertDetailMapper.mapData(response.value)
                is Resource.Failure -> throw response.serviceException
            }
        }

    override suspend fun resolveAlert(alertId: Int): Boolean =
        safeApiCall {
            alertService.resolveAlert(AlertResolveRequest(id = alertId))
        }.let { response ->
            when (response) {
                is Resource.Success -> response.value.ok
                is Resource.Failure -> throw response.serviceException
            }
        }
}