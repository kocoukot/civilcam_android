package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.SubscriptionPlan
import com.civilcam.domainLayer.model.alerts.GuardianAlertInformation

interface MockRepository {

    fun getCurrentSubscriptionPlan(): SubscriptionPlan

    suspend fun getMapAlert(): GuardianAlertInformation
}





