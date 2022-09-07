package com.civilcam.data.repository

import com.civilcam.R
import com.civilcam.domainLayer.model.SubscriptionPlan
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.domainLayer.model.alerts.GuardianAlertInformation
import com.civilcam.domainLayer.model.user.UserInfo
import com.civilcam.domainLayer.repos.MockRepository
import com.google.android.gms.maps.model.LatLng

class MockRepositoryImpl: MockRepository {

    override suspend fun getAlerts() = listOf(
       AlertModel(
           alertId = 2534,
           userInfo = UserInfo(
               userName = "Alleria Windrunner",
               avatar = R.drawable.img_avatar_one
           ),
           isResolved = false,
           alertDate = 1643803200000,
       ),
        AlertModel(
            alertId = 5345,
            userInfo = UserInfo(
                userName = "Arthas Menethil",
                avatar = R.drawable.img_avatar_two
            ),
            isResolved = false,
            alertDate = 1643803200000,
        ),
    )


    override suspend fun getHistoryAlert(alertType: AlertType) = when (alertType) {
        AlertType.RECEIVED -> listOf(
            AlertModel(
                alertId = 74545,
                userInfo = UserInfo(
                    userName = "Alleria Windrunner",
                    avatar = R.drawable.img_avatar_one
                ),
                isResolved = false,
                alertDate = 1643803200000,
            ),
            AlertModel(
                alertId = 27457,
                userInfo = UserInfo(
                    userName = "Arthas Menethil",
                    avatar = R.drawable.img_avatar_two
                ),
                isResolved = false,
                alertDate = 1643803200000,
            ),
        )
        AlertType.SENT -> listOf(
            AlertModel(
                alertId = 53468,
                userInfo = UserInfo(
                    userName = "Alleria Windrunner",
                    avatar = R.drawable.img_avatar_one
                ),
                isResolved = false,
                alertDate = 1643803200000,
            ),
            AlertModel(
                alertId = 6767,
                userInfo = UserInfo(
                    userName = "Arthas Menethil",
                    avatar = R.drawable.img_avatar_two
                ),
                isResolved = false,
                alertDate = 1643803200000,
            ),
        )
    }

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





