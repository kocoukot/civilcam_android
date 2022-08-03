package com.civilcam.data.network.model.response.auth

import com.google.gson.annotations.SerializedName

class SessionUserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("authType") val authType: String,
    @SerializedName("email") val email: String,
    @SerializedName("language") val language: String,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("isEmailVerified") val isEmailVerified: Boolean = false,
    @SerializedName("isTermsAndPolicyAccepted") val isTermsAndPolicyAccepted: Boolean = false,
    @SerializedName("isUserProfileSetupRequired") val isUserProfileSetupRequired: Boolean = true,
    @SerializedName("canChangeEmail") val canChangeEmail: Boolean,
    @SerializedName("canChangePassword") val canChangePassword: Boolean,
)