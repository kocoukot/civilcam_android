package com.civilcam.data.repository

import com.civilcam.domain.model.UserInfo
import com.civilcam.ui.profile.userDetails.model.UserDetailsModel

class MockRepository {

    suspend fun getUserInformation(userId: String) = UserDetailsModel(
        userInfoSection = UserInfo(
            userName = "Sylvanas Windrunner",
            dateOfBirth = 937821684000,
            address = "1456 Broadway, New York, NY 10023",
            phoneNumber = "+1 123 456 7890",
            avatar = "",
        ),
    )
}





