package com.civilcam.data.mapper.profile

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.request.profile.UserProfileRequest
import com.civilcam.domain.model.UserSetupModel
import com.civilcam.utils.DateUtils


class UserInfoToDomainMapper : Mapper<UserSetupModel, UserProfileRequest>(
    fromData = {
        System.currentTimeMillis()
        UserProfileRequest(
            firstName = it.firstName,
            lastName = it.lastName,
            dob = DateUtils.dateOfBirthFormat(it.dateBirth),
            phone = it.phoneNumber,
            address = it.location,
        )
    }
)