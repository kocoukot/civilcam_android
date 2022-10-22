package com.civilcam.data.repository

import com.civilcam.domainLayer.model.SubscriptionPlan
import com.civilcam.domainLayer.model.alerts.GuardianAlertInformation
import com.civilcam.domainLayer.repos.MockRepository
import com.google.android.gms.maps.model.LatLng

class MockRepositoryImpl: MockRepository {


    override fun getCurrentSubscriptionPlan() = SubscriptionPlan(
        subscriptionPeriod = "Monthly",
        subscriptionPlan = "$4.99/monthly",
        autoRenewDate = "02.02.2022"
    )

    override suspend fun getMapAlert(): GuardianAlertInformation = GuardianAlertInformation(
        userId = 23123,
        userName = "Sylvanas Windrunner",
        userPhoneNumber = "+15675473876",
        requestSent = "02.02.2022",
        userAddress = "12564 Nox Street, Chicago, IL 60607, USA",
        userLocation = LatLng(41.950188, -87.780036),
    )


}





