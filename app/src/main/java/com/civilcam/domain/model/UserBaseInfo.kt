package com.civilcam.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserBaseInfo(
	val id: Int? = null,
	val firstName: String? = null,
	val lastName: String? = null,
	val phone: String? = null,
	val location: String? = null,
) : Parcelable
