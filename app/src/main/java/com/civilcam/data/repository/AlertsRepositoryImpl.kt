package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage
import com.civilcam.data.mapper.alerts.AlertListMapper
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
    override suspend fun updateSosCoords(location: String, coords: LatLng): Boolean {
        return true
    }
//	: Boolean =
//		safeApiCall {
//			alertService.postSosCoordinates(
//				VerifyResetPasswordRequest(email, code)
//			)
//		}.let { response ->
//			when (response) {
//				is Resource.Success -> response.value.recoveryToken
//				is Resource.Failure -> throw response.serviceException
//
//			}
//		}

    override suspend fun getAlertsList(): List<AlertModel> =
        safeApiCall {
            alertService.getAlertsList(PaginationRequest())
        }.let { response ->
            when (response) {
                is Resource.Success -> response.value.list.map { alertListMapper.mapData(it) }
                is Resource.Failure -> throw response.serviceException

            }
        }

    override suspend fun getAlertsHistory(historyType: String): List<AlertModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlertDetail(alertId: Int): AlertDetailModel {
        TODO("Not yet implemented")
    }

    override suspend fun resolveAlert(alertId: Int): Boolean {
        TODO("Not yet implemented")
    }


}