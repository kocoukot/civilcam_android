package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage
import com.civilcam.data.network.service.AlertService
import com.civilcam.data.network.support.BaseRepository
import com.civilcam.domainLayer.model.alerts.AlertDetailModel
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.repos.AlertsRepository
import com.google.android.gms.maps.model.LatLng


class AlertsRepositoryImpl(
    private val accountStorage: AccountStorage,
    private val alertService: AlertService,
) : AlertsRepository, BaseRepository() {

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

    override suspend fun getAlertsList(): List<AlertModel> {
        TODO("Not yet implemented")
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