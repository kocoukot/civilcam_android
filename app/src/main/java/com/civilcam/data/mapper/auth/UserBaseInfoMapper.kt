package com.civilcam.data.mapper.auth

import com.civilcam.data.mapper.ImageInfoMapper
import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.auth.UseBaseInfoResponse

class UserBaseInfoMapper(
    private val imageInfoMapper: ImageInfoMapper = ImageInfoMapper()
) : Mapper<UseBaseInfoResponse, com.civilcam.domainLayer.model.UserBaseInfo>(
    fromData = { response ->
        com.civilcam.domainLayer.model.UserBaseInfo(
            avatar = response.avatar?.let { imageInfoMapper.mapData(it) },
            firstName = response.firstName,
            lastName = response.lastName,
            dob = response.dob.orEmpty(),
            address = response.address.orEmpty(),
            phone = response.phone.orEmpty(),
            isPhoneVerified = response.isPhoneVerified,
        )
    }
)