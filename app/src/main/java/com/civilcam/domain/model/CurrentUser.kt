package com.civilcam.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentUser(
    val accessToken: String? = "",
    val sessionUser: SessionUser = SessionUser(),
    val userBaseInfo: UserBaseInfo = UserBaseInfo(),
) : Parcelable
