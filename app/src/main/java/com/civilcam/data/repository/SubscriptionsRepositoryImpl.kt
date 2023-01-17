package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage
import com.civilcam.data.mapper.auth.UserMapper
import com.civilcam.data.mapper.subscriptions.SubscriptionBaseInfoMapper
import com.civilcam.data.mapper.subscriptions.SubscriptionInfoMapper
import com.civilcam.data.mapper.subscriptions.SubscriptionsMapper
import com.civilcam.data.network.model.request.subscriptions.SubscriptionRequest
import com.civilcam.data.network.model.request.subscriptions.TrialSubscriptionRequest
import com.civilcam.data.network.service.SubscriptionsService
import com.civilcam.data.network.support.BaseRepository
import com.civilcam.data.network.support.Resource
import com.civilcam.domainLayer.model.subscription.SubscriptionBaseInfo
import com.civilcam.domainLayer.model.subscription.SubscriptionsList
import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.domainLayer.repos.SubscriptionsRepository

class SubscriptionsRepositoryImpl(
	private val subscriptionsService: SubscriptionsService,
	private val accountStorage: AccountStorage
) : SubscriptionsRepository, BaseRepository() {
	
	private val userMapper: UserMapper = UserMapper()
	
	val mapper: SubscriptionsMapper = SubscriptionsMapper()
	private val subscriptionMapper: SubscriptionInfoMapper = SubscriptionInfoMapper()
	private val subscriptionBaseInfoMapper: SubscriptionBaseInfoMapper =
		SubscriptionBaseInfoMapper()
	
	override suspend fun getSubscriptionsList(): SubscriptionsList = safeApiCall {
		subscriptionsService.getSubscriptionsList()
	}.let { response ->
		when (response) {
			is Resource.Success -> mapper.mapData(response.value)
			is Resource.Failure -> {
				throw response.serviceException
			}
		}
	}
	
	override suspend fun getUserSubscription(): SubscriptionBaseInfo = safeApiCall {
		subscriptionsService.getUserSubscription()
	}.let { response ->
		when (response) {
			is Resource.Success -> subscriptionMapper.mapData(response.value)
			is Resource.Failure -> {
				throw response.serviceException
			}
		}
	}
	
	override suspend fun setGoogleSubscription(
		receipt: String,
		productId: String
	): CurrentUser =
		safeApiCall {
			subscriptionsService.setGoogleSubscription(SubscriptionRequest(receipt, productId))
		}.let { response ->
			when (response) {
				is Resource.Success -> {
					userMapper.mapData(response.value)
						.also { accountStorage.updateUser(it) }
				}
				is Resource.Failure -> {
					throw response.serviceException
				}
			}
		}
	
	override suspend fun setTrialSubscription(productId: String): CurrentUser =
		safeApiCall {
			subscriptionsService.setTrialSubscription(TrialSubscriptionRequest(productId))
		}.let { response ->
			when (response) {
				is Resource.Success -> {
					userMapper.mapData(response.value)
						.also { accountStorage.updateUser(it) }
				}
				is Resource.Failure -> {
					throw response.serviceException
				}
			}
		}
}