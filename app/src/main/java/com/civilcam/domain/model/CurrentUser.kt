package com.civilcam.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentUser(
	val sessionUser: SessionUser,
	val userBaseInfo: UserBaseInfo,
	val accessToken: String?
) : Parcelable
