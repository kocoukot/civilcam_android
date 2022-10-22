package com.civilcam.data.mapper.subscriptions

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.subscriptions.SubscriptionsListResponse
import com.civilcam.domainLayer.model.subscription.SubscriptionsList

class SubscriptionsMapper : Mapper<SubscriptionsListResponse, SubscriptionsList>(
	fromData = { response ->
		SubscriptionsList(
			response.subsList.map { item ->
				SubscriptionsList.SubscriptionInfo(
					item.productId,
					item.title,
					item.description,
					item.cost,
					item.term,
					item.unitType
				)
			}
		)
	}
)