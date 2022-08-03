package com.civilcam.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SessionUser(
    val id: Int,
    val email: String,
    val language: String,
    val fullName: String,
    val isEmailVerified: Boolean,
    val isTermsAndPolicyAccepted: Boolean,
    val isUserProfileSetupRequired: Boolean,
    val canChangeEmail: Boolean,
    val canChangePassword: Boolean
) : Parcelable
