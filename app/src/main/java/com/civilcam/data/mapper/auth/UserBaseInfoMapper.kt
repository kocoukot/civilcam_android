package com.civilcam.data.mapper.auth

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.auth.UseBaseInfoResponse
import com.civilcam.domain.model.UserBaseInfo

class UserBaseInfoMapper : Mapper<UseBaseInfoResponse, UserBaseInfo>(
    fromData = { response ->
        response.let {
            UserBaseInfo(
                avatar = it.avatar,
                firstName = it.firstName,
                lastName = it.lastName,
                dob = it.dob,
                address = it.address,
                phone = it.phone,
                isPhoneVerified = it.isPhoneVerified,
            )
        }
    }
)