package com.civilcam.domainLayer.model.user

import android.os.Parcelable
import com.civilcam.domainLayer.model.subscription.SubscriptionBaseInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentUser(
    val accessToken: String? = "",
    val sessionUser: SessionUser = SessionUser(),
    val userBaseInfo: UserBaseInfo = UserBaseInfo(),
    val settings: UserSettings = UserSettings(),
    val subscription: SubscriptionBaseInfo = SubscriptionBaseInfo()
) : Parcelable
