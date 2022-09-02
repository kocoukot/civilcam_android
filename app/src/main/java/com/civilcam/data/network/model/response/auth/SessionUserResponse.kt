package com.civilcam.data.network.model.response.auth

import com.civilcam.domainLayer.model.AuthType
import com.civilcam.domainLayer.model.user.LanguageType
import com.google.gson.annotations.SerializedName

class SessionUserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("authType") val authType: AuthType,
    @SerializedName("email") val email: String? = "",
    @SerializedName("language") val language: LanguageType,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("isEmailVerified") val isEmailVerified: Boolean = false,
    @SerializedName("isTermsAndPolicyAccepted") val isTermsAndPolicyAccepted: Boolean = false,
    @SerializedName("isUserProfileSetupRequired") val isUserProfileSetupRequired: Boolean = true,
    @SerializedName("isPinCodeSet") val isPinCodeSet: Boolean,
    @SerializedName("canChangeEmail") val canChangeEmail: Boolean,
    @SerializedName("canChangePassword") val canChangePassword: Boolean
)