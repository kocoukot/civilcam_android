package com.civilcam.domainLayer.model.subscription

import android.os.Parcelable
import com.civilcam.domainLayer.model.SubscriptionStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubscriptionBaseInfo(
	val id: Int,
	val productId: String?,
	val title: String,
	val cost: Int,
	val term: Int,
	val unitType: String,
	val expiredAt: String,
	val status: SubscriptionStatus
) : Parcelable