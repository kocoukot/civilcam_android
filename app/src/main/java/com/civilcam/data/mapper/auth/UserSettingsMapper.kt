package com.civilcam.data.mapper.auth

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.user.UserSettingsResponse
import com.civilcam.domainLayer.model.user.UserSettings

class UserSettingsMapper : Mapper<UserSettingsResponse, UserSettings>(
    fromData = {
        UserSettings(
            smsNotifications = it.isEmailAlertOn,
            emailNotification = it.isSmsAlertOn,
            faceId = it.isFaceIdOn,
        )
    }
)