package com.civilcam.data.mapper.subscriptions

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.subscriptions.SubscriptionResponse
import com.civilcam.domainLayer.model.subscription.SubscriptionBaseInfo

class SubscriptionBaseInfoMapper :
	Mapper<SubscriptionResponse.UserSubscriptionResponse, SubscriptionBaseInfo>(fromData = { response ->
		SubscriptionBaseInfo(
			response.id,
			response.productId,
			response.title,
			response.cost,
			response.term,
			response.unitType,
			response.expiredAt,
			response.status
		)
	})