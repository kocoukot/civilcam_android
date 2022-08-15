package com.civilcam.data.mapper.auth

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.auth.SessionUserResponse
import com.civilcam.domainLayer.model.SessionUser

class SessionUserMapper
	: Mapper<SessionUserResponse, SessionUser>(
	fromData = { response ->
		response.let {
			SessionUser(
				id = it.id,
				authType = it.authType,
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