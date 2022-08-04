package com.civilcam.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserBaseInfo(
    val avatar: ImageInfo? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val dob: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val isPhoneVerified: Boolean = false,
) : Parcelable