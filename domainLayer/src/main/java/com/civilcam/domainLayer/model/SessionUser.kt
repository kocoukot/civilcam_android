package com.civilcam.domainLayer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SessionUser(
    val id: Int = 0,
    val authType: AuthType = AuthType.EMAIL,
    val email: String = "",
    val language: LanguageType = LanguageType.ENGLISH,
    val fullName: String = "",
    val isEmailVerified: Boolean = false,
    val isTermsAndPolicyAccepted: Boolean = false,
    val isUserProfileSetupRequired: Boolean = false,
    val canChangeEmail: Boolean = false,
    val canChangePassword: Boolean = false
) : Parcelable
