package com.civilcam.data.repository

import com.civilcam.R
import com.civilcam.domain.model.UserInfo
import com.civilcam.domain.model.alerts.AlertModel
import com.civilcam.domain.model.alerts.AlertType
import com.civilcam.ui.profile.userDetails.model.UserDetailsModel

class MockRepository {

    suspend fun getUserInformation(userId: String) = UserDetailsModel(
        userInfoSection = UserInfo(
            userName = "Sylvanas Windrunner",
            dateOfBirth = 937821684000,
            address = "1456 Broadway, New York, NY 10023",
            phoneNumber = "+1 123 456 7890",
            avatar = R.drawable.img_avatar,
        ),
    )

    suspend fun getAlerts() = listOf(
        AlertModel(
            alertId = 342342,
            userInfo = UserInfo(
                userName = "Alleria Windrunner",
                avatar = R.drawable.img_avatar_one
            ),
            isResolved = false,
            alertDate = 1643803200000,
        ),
        AlertModel(
            alertId = 634634,
            userInfo = UserInfo(userName = "Arthas Menethil", avatar = R.drawable.img_avatar_two),
            isResolved = false,
            alertDate = 1643803200000,
        ),
    )


    suspend fun getHistoryAlert(alertType: AlertType) = when (alertType) {
        AlertType.RECEIVED -> listOf(
            AlertModel(
                alertId = 342342,
                userInfo = UserInfo(
                    userName = "Alleria Windrunner",
                    avatar = R.drawable.img_avatar_one
                ),
                isResolved = false,
                alertDate = 1643803200000,
            ),
            AlertModel(
                alertId = 634634,
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
                alertId = 342342,
                userInfo = UserInfo(
                    userName = "Alleria Windrunner",
                    avatar = R.drawable.img_avatar_one
                ),
                isResolved = false,
                alertDate = 1643803200000,
            ),
            AlertModel(
                alertId = 634634,
                userInfo = UserInfo(
                    userName = "Arthas Menethil",
                    avatar = R.drawable.img_avatar_two
                ),
                isResolved = false,
                alertDate = 1643803200000,
            ),
        )
    }


}





