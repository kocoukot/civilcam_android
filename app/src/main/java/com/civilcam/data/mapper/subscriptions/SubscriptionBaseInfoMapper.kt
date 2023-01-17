package com.civilcam.data.mapper.subscriptions

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.subscriptions.SubscriptionResponse
import com.civilcam.domainLayer.model.subscription.SubscriptionBaseInfo

class SubscriptionBaseInfoMapper :
	Mapper<SubscriptionResponse.UserSubscriptionResponse, SubscriptionBaseInfo>(fromData = { response ->
		SubscriptionBaseInfo(
			id = response.id,
			productId = response.productId,
			title = response.title,
			cost = response.cost,
			term = response.term,
			unitType = response.unitType,
			expiredAt = response.expiredAt,
			status = response.status
		)
	})