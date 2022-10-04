package com.civilcam.domainLayer.model.subscription

import android.os.Parcelable
import com.civilcam.domainLayer.model.SubscriptionStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubscriptionBaseInfo(
	val id: Int = 0,
	val productId: String? = "",
	val title: String = "",
	val cost: Int = 0,
	val term: Int = 0,
	val unitType: String = "",
	val expiredAt: String = "",
	val status: SubscriptionStatus = SubscriptionStatus.disabled
) : Parcelable