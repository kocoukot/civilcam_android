package com.civilcam.data.mapper.auth

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.auth.SessionUserResponse

class SessionUserMapper
    : Mapper<SessionUserResponse, com.civilcam.domainLayer.model.SessionUser>(
    fromData = { response ->
        response.let {
            com.civilcam.domainLayer.model.SessionUser(
                id = it.id,
                email = it.email,
                language = it.language,
                fullName = it.fullName,
                isEmailVerified = it.isEmailVerified,
                isTermsAndPolicyAccepted = it.isTermsAndPolicyAccepted,
                isUserProfileSetupRequired = it.isUserProfileSetupRequired,
                canChangeEmail = it.canChangeEmail,
                canChangePassword = it.canChangePassword,
            )
        }
    }
)