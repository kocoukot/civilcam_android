package com.civilcam.data.mapper.auth

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.auth.UserResponse
import com.civilcam.domainLayer.model.CurrentUser

class UserMapper(
    private val sessionUserMapper: SessionUserMapper = SessionUserMapper(),
    private val userBaseInfoMapper: UserBaseInfoMapper = UserBaseInfoMapper(),

    ) : Mapper<UserResponse, CurrentUser>(
    fromData = { response ->
        response.let {
            CurrentUser(
                accessToken = it.accessToken,
                sessionUser = sessionUserMapper.mapData(it.sessionUser),
                userBaseInfo = userBaseInfoMapper.mapData(it.userProfile),
            )
        }
    }
)