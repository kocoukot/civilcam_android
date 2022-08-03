package com.civilcam.data.network.service

import com.civilcam.data.network.Endpoint
import com.civilcam.data.network.model.response.subscriptions.SubscriptionsListResponse
import retrofit2.http.GET

interface SubscriptionsService {

    @GET(Endpoint.Subscriptions.GET_SUBSCRIPTIONS_LIST)
    suspend fun getSubscriptionsList(): SubscriptionsListResponse

}


