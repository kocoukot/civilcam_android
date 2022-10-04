package com.civilcam.data.network.service

import com.civilcam.data.network.Endpoint
import com.civilcam.data.network.model.request.subscriptions.SubscriptionRequest
import com.civilcam.data.network.model.response.subscriptions.SubscriptionResponse
import com.civilcam.data.network.model.response.subscriptions.SubscriptionsListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SubscriptionsService {
	
	@GET(Endpoint.Subscriptions.GET_SUBSCRIPTIONS_LIST)
	suspend fun getSubscriptionsList(): SubscriptionsListResponse
	
	@GET(Endpoint.Subscriptions.GET_USER_SUBSCRIPTION)
	suspend fun getUserSubscription(): SubscriptionResponse
	
	@POST(Endpoint.Subscriptions.SET_GOOGLE_SUBSCRIPTION)
	suspend fun setGoogleSubscription(@Body request: SubscriptionRequest): SubscriptionResponse.UserSubscriptionResponse
	
}


