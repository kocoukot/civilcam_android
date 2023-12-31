package com.civilcam.data.mapper.auth

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.auth.SessionUserResponse
import com.civilcam.domainLayer.model.user.SessionUser
import com.civilcam.domainLayer.model.user.UserState

class SessionUserMapper : Mapper<SessionUserResponse, SessionUser>(
	fromData = {
		SessionUser(
            id = it.id,
            authType = it.authType,
            email = it.email.orEmpty(),
            language = it.language,
            fullName = it.fullName,
            isEmailVerified = it.isEmailVerified,
            isTermsAndPolicyAccepted = it.isTermsAndPolicyAccepted,
            isUserProfileSetupRequired = it.isUserProfileSetupRequired,
            isSubscriptionActive = it.isSubscriptionActive,
            isPinCodeSet = it.isPinCodeSet,
            canChangeEmail = it.canChangeEmail,
            canChangePassword = it.canChangePassword,
            userState = UserState.byDomain(it.state)
        )
	}
)