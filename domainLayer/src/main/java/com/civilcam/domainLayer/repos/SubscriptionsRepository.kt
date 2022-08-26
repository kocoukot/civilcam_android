package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.subscription.SubscriptionsList

interface SubscriptionsRepository {
	
	suspend fun getSubscriptionsList(): SubscriptionsList
	
}