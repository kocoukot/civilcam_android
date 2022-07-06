package com.civilcam.data.repository

import com.civilcam.R
import com.civilcam.domain.model.UserInfo
import com.civilcam.domain.model.UserSetupModel
import com.civilcam.domain.model.alerts.AlertModel
import com.civilcam.domain.model.alerts.AlertType
import com.civilcam.domain.model.guard.GuardianModel
import com.civilcam.domain.model.guard.GuardianStatus
import com.civilcam.domain.model.guard.NetworkType
import com.civilcam.ui.network.main.model.GuardianItem
import com.civilcam.ui.profile.userDetails.model.UserDetailsModel

class MockRepository {

    suspend fun getUserInformation(userId: String) = UserDetailsModel(
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

    fun getGuards(guardType: NetworkType) =
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


    fun getGuardRequests() = listOf(
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

    fun checkPassword(currentPassword: String) = currentPassword == "Password1@"


    fun searchGuards(query: String) =
        listOf(
            GuardianModel(
                guardianId = 16434,
                guardianName = "Alleria Windrunner",
                guardianAvatar = R.drawable.img_avatar_one,
                guardianStatus = GuardianStatus.NEW,
            ),
            GuardianModel(
                guardianId = 335,
                guardianName = "Arthas Menethil",
                guardianAvatar = R.drawable.img_avatar_two,
                guardianStatus = GuardianStatus.NEW,
            ),
            GuardianModel(
                guardianId = 5243,
                guardianName = "Bobby Axelrod",
                guardianAvatar = R.drawable.img_avatar_three,
                guardianStatus = GuardianStatus.NEW,
            ),
            GuardianModel(
                guardianId = 4324,
                guardianName = "Bane Hardy",
                guardianAvatar = R.drawable.img_avatar_four,
                guardianStatus = GuardianStatus.NEW,
            ),
            GuardianModel(
                guardianId = 35432,
                guardianName = "Sylvanas Windrunner",
                guardianAvatar = R.drawable.img_avatar,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
            GuardianModel(
                guardianId = 16434,
                guardianName = "Alleria Windrunner",
                guardianAvatar = R.drawable.img_avatar_one,
                guardianStatus = GuardianStatus.PENDING,
            ),
            GuardianModel(
                guardianId = 335,
                guardianName = "Arthas Menethil",
                guardianAvatar = R.drawable.img_avatar_two,
                guardianStatus = GuardianStatus.NEW,
            ),
            GuardianModel(
                guardianId = 5243,
                guardianName = "Bobby Axelrod",
                guardianAvatar = R.drawable.img_avatar_three,
                guardianStatus = GuardianStatus.PENDING,
            ),
            GuardianModel(
                guardianId = 4324,
                guardianName = "Bane Hardy",
                guardianAvatar = R.drawable.img_avatar_four,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
            GuardianModel(
                guardianId = 35432,
                guardianName = "Sylvanas Windrunner",
                guardianAvatar = R.drawable.img_avatar,
                guardianStatus = GuardianStatus.PENDING,
            ),
            GuardianModel(
                guardianId = 16434,
                guardianName = "Alleria Windrunner",
                guardianAvatar = R.drawable.img_avatar_one,
                guardianStatus = GuardianStatus.NEW,
            ),
            GuardianModel(
                guardianId = 335,
                guardianName = "Arthas Menethil",
                guardianAvatar = R.drawable.img_avatar_two,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
            GuardianModel(
                guardianId = 5243,
                guardianName = "Bobby Axelrod",
                guardianAvatar = R.drawable.img_avatar_three,
                guardianStatus = GuardianStatus.DECLINED,
            ),
            GuardianModel(
                guardianId = 4324,
                guardianName = "Bane Hardy",
                guardianAvatar = R.drawable.img_avatar_four,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
            GuardianModel(
                guardianId = 35432,
                guardianName = "Sylvanas Windrunner",
                guardianAvatar = R.drawable.img_avatar,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
            GuardianModel(
                guardianId = 16434,
                guardianName = "Alleria Windrunner",
                guardianAvatar = R.drawable.img_avatar_one,
                guardianStatus = GuardianStatus.PENDING,
            ),
            GuardianModel(
                guardianId = 335,
                guardianName = "Arthas Menethil",
                guardianAvatar = R.drawable.img_avatar_two,
                guardianStatus = GuardianStatus.DECLINED,
            ),
            GuardianModel(
                guardianId = 5243,
                guardianName = "Bobby Axelrod",
                guardianAvatar = R.drawable.img_avatar_three,
                guardianStatus = GuardianStatus.DECLINED,
            ),
            GuardianModel(
                guardianId = 4324,
                guardianName = "Bane Hardy",
                guardianAvatar = R.drawable.img_avatar_four,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
            GuardianModel(
                guardianId = 35432,
                guardianName = "Sylvanas Windrunner",
                guardianAvatar = R.drawable.img_avatar,
                guardianStatus = GuardianStatus.ACCEPTED,
            ),
        ).filter { it.guardianName.contains(query, true) }
}





