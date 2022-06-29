package com.civilcam.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SessionUser(
	val id: Int,
	val sessionToken: String,
	val email: String,
	val isEmailVerified: Boolean
) : Parcelable
