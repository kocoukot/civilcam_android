package com.civilcam.data.mapper.profile

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.request.profile.UserProfileRequest
import com.civilcam.domainLayer.model.profile.UserSetupModel


class UserInfoToDomainMapper : Mapper<UserSetupModel, UserProfileRequest>(
    fromData = {
        UserProfileRequest(
            firstName = it.firstName,
            lastName = it.lastName,
            dob = it.dateBirth,
            phone = it.phoneNumber,
            address = it.location,
        )
    }
)