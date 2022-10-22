package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.subscription.SubscriptionBaseInfo
import com.civilcam.domainLayer.model.subscription.SubscriptionsList

interface SubscriptionsRepository {
	
	suspend fun getSubscriptionsList(): SubscriptionsList
	
	suspend fun getUserSubscription(): SubscriptionBaseInfo
	
	suspend fun setGoogleSubscription(receipt: String, productId: String): SubscriptionBaseInfo
	
}