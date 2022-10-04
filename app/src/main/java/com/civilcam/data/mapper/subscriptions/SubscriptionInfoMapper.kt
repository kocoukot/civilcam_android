package com.civilcam.data.mapper.subscriptions

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.subscriptions.SubscriptionResponse
import com.civilcam.domainLayer.model.subscription.SubscriptionBaseInfo

class SubscriptionInfoMapper :
	Mapper<SubscriptionResponse, SubscriptionBaseInfo>(fromData = { response ->
		SubscriptionBaseInfo(
			response.subscription.id,
			response.subscription.productId,
			response.subscription.title,
			response.subscription.cost,
			response.subscription.term,
			response.subscription.unitType,
			response.subscription.expiredAt,
			response.subscription.status
		)
	})