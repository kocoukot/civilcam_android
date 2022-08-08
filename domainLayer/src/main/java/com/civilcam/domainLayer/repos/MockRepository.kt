package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.SubscriptionPlan
import com.civilcam.domainLayer.model.UserDetailsModel
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.domainLayer.model.guard.GuardianItem
import com.civilcam.domainLayer.model.guard.GuardianModel
import com.civilcam.domainLayer.model.guard.NetworkType

interface MockRepository {

    suspend fun getUserInformation(userId: String): UserDetailsModel

    suspend fun getAlerts(): List<AlertModel>

    suspend fun getHistoryAlert(alertType: AlertType): List<AlertModel>

    fun getGuards(guardType: NetworkType): List<GuardianItem>


    fun getGuardRequests(): List<GuardianItem>

    fun checkPassword(currentPassword: String): Boolean

    fun getCurrentSubscriptionPlan(): SubscriptionPlan

    fun searchGuards(query: String): List<GuardianModel>
}





