package com.civilcam.data.mapper.auth

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.mapper.subscriptions.SubscriptionBaseInfoMapper
import com.civilcam.data.network.model.response.auth.UserResponse
import com.civilcam.domainLayer.model.subscription.SubscriptionBaseInfo
import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.domainLayer.model.user.UserSettings

class UserMapper(
    private val sessionUserMapper: SessionUserMapper = SessionUserMapper(),
    private val userBaseInfoMapper: UserBaseInfoMapper = UserBaseInfoMapper(),
    private val subscriptionMapper: SubscriptionBaseInfoMapper = SubscriptionBaseInfoMapper()
    ) : Mapper<UserResponse, CurrentUser>(
    fromData = { response ->
        response.let {
            CurrentUser(
                accessToken = it.accessToken,
                sessionUser = sessionUserMapper.mapData(it.sessionUser),
                userBaseInfo = userBaseInfoMapper.mapData(it.userProfile),
                settings = UserSettings(
                    smsNotifications = it.settings.isSmsAlertOn,
                    emailNotification = it.settings.isEmailAlertOn,
                    faceId = it.settings.isFaceIdOn,
                ),
                subscription = it.subscription?.let { data -> subscriptionMapper.mapData(data) }
            )
        }
    }
)