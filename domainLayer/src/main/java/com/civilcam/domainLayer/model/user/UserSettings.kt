package com.civilcam.domainLayer.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSettings(
    val smsNotifications: Boolean = true,
    val emailNotification: Boolean = true,
    val faceId: Boolean = true,
) : Parcelable