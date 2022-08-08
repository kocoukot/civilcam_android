package com.civilcam.data.mapper.auth

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.auth.UserResponse

class UserMapper(
    private val sessionUserMapper: SessionUserMapper = SessionUserMapper(),
    private val userBaseInfoMapper: UserBaseInfoMapper = UserBaseInfoMapper(),

    ) : Mapper<UserResponse, com.civilcam.domainLayer.model.CurrentUser>(
    fromData = { response ->
        response.let {
            com.civilcam.domainLayer.model.CurrentUser(
                accessToken = it.accessToken,
                sessionUser = sessionUserMapper.mapData(it.sessionUser),
                userBaseInfo = userBaseInfoMapper.mapData(it.userProfile),
            )
        }
    }
)