package com.civilcam.data.repository

import com.civilcam.data.mapper.subscriptions.SubscriptionsMapper
import com.civilcam.data.network.service.SubscriptionsService
import com.civilcam.data.network.support.BaseRepository
import com.civilcam.data.network.support.Resource
import com.civilcam.domainLayer.model.subscription.SubscriptionsList
import com.civilcam.domainLayer.repos.SubscriptionsRepository

class SubscriptionsRepositoryImpl(
	private val subscriptionsService: SubscriptionsService
) : SubscriptionsRepository, BaseRepository() {
	
	val mapper: SubscriptionsMapper = SubscriptionsMapper()
	
	override suspend fun getSubscriptionsList(): SubscriptionsList =
		safeApiCall {
			subscriptionsService.getSubscriptionsList()
		}.let { response ->
			when (response) {
				is Resource.Success -> mapper.mapData(response.value)
				is Resource.Failure -> {
					throw response.serviceException
				}
			}
		}
}