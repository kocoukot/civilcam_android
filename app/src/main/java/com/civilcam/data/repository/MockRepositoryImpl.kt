package com.civilcam.data.repository

import com.civilcam.R
import com.civilcam.domainLayer.model.SubscriptionPlan
import com.civilcam.domainLayer.model.UserDetailsModel
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.domainLayer.model.alerts.GuardianAlertInformation
import com.civilcam.domainLayer.model.guard.GuardianItem
import com.civilcam.domainLayer.model.guard.GuardianModel
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.domainLayer.model.guard.NetworkType
import com.civilcam.domainLayer.model.user.UserInfo
import com.civilcam.domainLayer.repos.MockRepository
import com.google.android.gms.maps.model.LatLng

class MockRepositoryImpl: MockRepository {

    override suspend fun getUserInformation(userId: String) = UserDetailsModel(
        userInfoSection = UserInfo(
            userName = "Sylvanas Windrunner",
            dateOfBirth = 937821684000,
            address = "1456 Broadway, New York, NY 10023",
            phoneNumber = "+1 (123) 456 7890",
            avatar = R.drawable.img_avatar,
            email = "sylvanas.w@mail.com",
            pinCode = "••••",
            firstName = "Sylvanas",
            lastName = "Windrunner"
        ),
    )

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

    override fun getGuards(guardType: NetworkType) =
        when (guardType) {
            NetworkType.ON_GUARD -> listOf(
                GuardianItem(
                    guardianId = 1,
                    guardianName = "Alleria Windrunner",
                    guardianAvatar = R.drawable.img_avatar_one,
                    guardianStatus = GuardianStatus.NEED_HELP,
                ),
                GuardianItem(
                    guardianId = 3,
                    guardianName = "Arthas Menethil",
                    guardianAvatar = R.drawable.img_avatar_two,
                    guardianStatus = GuardianStatus.SAFE,
                ),
                GuardianItem(
                    guardianId = 43,
                    guardianName = "Bobby Axelrod",
                    guardianAvatar = R.drawable.img_avatar_three,
                    guardianStatus = GuardianStatus.SAFE,
                ),
                GuardianItem(
                    guardianId = 345,
                    guardianName = "Bane Hardy",
                    guardianAvatar = R.drawable.img_avatar_four,
                    guardianStatus = GuardianStatus.SAFE,
                )
            )
            NetworkType.GUARDIANS -> listOf(
                GuardianItem(
                    guardianId = 1,
                    guardianName = "Alleria Windrunner",
                    guardianAvatar = R.drawable.img_avatar_one,
                    guardianStatus = GuardianStatus.PENDING,
                ),
                GuardianItem(
                    guardianId = 3,
                    guardianName = "Arthas Menethil",
                    guardianAvatar = R.drawable.img_avatar_two,
                    guardianStatus = GuardianStatus.DECLINED,
                ),
                GuardianItem(
                    guardianId = 43,
                    guardianName = "Bobby Axelrod",
                    guardianAvatar = R.drawable.img_avatar_three,
                    guardianStatus = GuardianStatus.DECLINED,
                ),
                GuardianItem(
                    guardianId = 345,
                    guardianName = "Bane Hardy",
                    guardianAvatar = R.drawable.img_avatar_four,
                    guardianStatus = GuardianStatus.ACCEPTED,
                )
            )
        }


    override fun getGuardRequests() = listOf(
        GuardianItem(
            guardianId = 1,
            guardianName = "Alleria Windrunner",
            guardianAvatar = R.drawable.img_avatar_one,
            guardianStatus = GuardianStatus.PENDING,
        ),
        GuardianItem(
            guardianId = 3,
            guardianName = "Arthas Menethil",
            guardianAvatar = R.drawable.img_avatar_two,
            guardianStatus = GuardianStatus.DECLINED,
        ),
        GuardianItem(
            guardianId = 43,
            guardianName = "Bane Hardy",
            guardianAvatar = R.drawable.img_avatar_four,
            guardianStatus = GuardianStatus.DECLINED,
        ),
    )

    override fun checkPassword(currentPassword: String) = currentPassword == "Password1@"

   override  fun getCurrentSubscriptionPlan() = SubscriptionPlan(
        subscriptionPeriod = "Monthly",
        subscriptionPlan = "$4.99/monthly",
        autoRenewDate = "02.02.2022"
    )

   override  fun searchGuards(query: String) =
        listOf(
            GuardianModel(
                guardianId = 16434,
                guardianName = "Alleria Windrunner",
                guardianAvatar = R.drawable.img_avatar_one,
                guardianStatus = GuardianStatus.NEW,
            ),
            GuardianModel(
                guardianId = 848567,
                guardianName = "Arthas Menethil",
                guardianAvatar = R.drawable.img_avatar_two,
                guardianStatus = GuardianStatus.NEW,
            ),
            GuardianModel(
                guardianId = 63457,
                guardianName = "Bobby Axelrod",
                guardianAvatar = R.drawable.img_avatar_three,
                guardianStatus = GuardianStatus.NEW,
            ),
            GuardianModel(
                guardianId = 53452,
                guardianName = "Bane Hardy",
                guardianAvatar = R.drawable.img_avatar_four,
                guardianStatus = GuardianStatus.NEW,
            ),
            GuardianModel(
                guardianId = 67462,
                guardianName = "Sylvanas Windrunner",
                guardianAvatar = R.drawable.img_avatar,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
            GuardianModel(
                guardianId = 64566,
                guardianName = "Alleria Windrunner",
                guardianAvatar = R.drawable.img_avatar_one,
                guardianStatus = GuardianStatus.PENDING,
            ),
            GuardianModel(
                guardianId = 6065456,
                guardianName = "Arthas Menethil",
                guardianAvatar = R.drawable.img_avatar_two,
                guardianStatus = GuardianStatus.NEW,
            ),
            GuardianModel(
                guardianId = 673,
                guardianName = "Bobby Axelrod",
                guardianAvatar = R.drawable.img_avatar_three,
                guardianStatus = GuardianStatus.PENDING,
            ),
            GuardianModel(
                guardianId = 589,
                guardianName = "Bane Hardy",
                guardianAvatar = R.drawable.img_avatar_four,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
            GuardianModel(
                guardianId = 664,
                guardianName = "Sylvanas Windrunner",
                guardianAvatar = R.drawable.img_avatar,
                guardianStatus = GuardianStatus.PENDING,
            ),
            GuardianModel(
                guardianId = 158,
                guardianName = "Alleria Windrunner",
                guardianAvatar = R.drawable.img_avatar_one,
                guardianStatus = GuardianStatus.NEW,
            ),
            GuardianModel(
                guardianId = 521,
                guardianName = "Arthas Menethil",
                guardianAvatar = R.drawable.img_avatar_two,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
            GuardianModel(
                guardianId = 675,
                guardianName = "Bobby Axelrod",
                guardianAvatar = R.drawable.img_avatar_three,
                guardianStatus = GuardianStatus.DECLINED,
            ),
            GuardianModel(
                guardianId = 765,
                guardianName = "Bane Hardy",
                guardianAvatar = R.drawable.img_avatar_four,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
            GuardianModel(
                guardianId = 245,
                guardianName = "Sylvanas Windrunner",
                guardianAvatar = R.drawable.img_avatar,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
            GuardianModel(
                guardianId = 89556,
                guardianName = "Alleria Windrunner",
                guardianAvatar = R.drawable.img_avatar_one,
                guardianStatus = GuardianStatus.PENDING,
            ),
            GuardianModel(
                guardianId = 783345,
                guardianName = "Arthas Menethil",
                guardianAvatar = R.drawable.img_avatar_two,
                guardianStatus = GuardianStatus.DECLINED,
            ),
            GuardianModel(
                guardianId = 8467837,
                guardianName = "Bobby Axelrod",
                guardianAvatar = R.drawable.img_avatar_three,
                guardianStatus = GuardianStatus.DECLINED,
            ),
            GuardianModel(
                guardianId = 65745,
                guardianName = "Bane Hardy",
                guardianAvatar = R.drawable.img_avatar_four,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
            GuardianModel(
                guardianId = 8334563,
                guardianName = "Sylvanas Windrunner",
                guardianAvatar = R.drawable.img_avatar,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
        ).filter { it.guardianName.contains(query, true) }

    override suspend fun getMapAlert(): GuardianAlertInformation = GuardianAlertInformation(
        userId = 23123,
        userName = "Sylvanas Windrunner",
        userPhoneNumber = "+15675473876",
        requestSent = "02.02.2022",
        userAddress = "12564 Nox Street, Chicago, IL 60607, USA",
        userLocation = LatLng(41.950188, -87.780036),
    )


}





