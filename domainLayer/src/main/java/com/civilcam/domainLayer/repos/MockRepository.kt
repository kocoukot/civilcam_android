package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.SubscriptionPlan
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.domainLayer.model.alerts.GuardianAlertInformation

interface MockRepository {


    suspend fun getAlerts(): List<AlertModel>

    suspend fun getHistoryAlert(alertType: AlertType): List<AlertModel>

    fun getCurrentSubscriptionPlan(): SubscriptionPlan

    suspend fun getMapAlert(): GuardianAlertInformation
}





