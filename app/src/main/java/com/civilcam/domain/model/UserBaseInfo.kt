package com.civilcam.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserBaseInfo(
    var avatar: ImageInfo? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var dob: String = "",
    var address: String = "",
    var phone: String = "",
    var isPhoneVerified: Boolean = false,
) : Parcelable