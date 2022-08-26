package com.civilcam.data.network.service

import com.civilcam.data.network.Endpoint
import com.civilcam.data.network.model.request.auth.GoogleOAuthRequest
import com.civilcam.data.network.model.response.auth.GoogleOAuthResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface GoogleOAuthService {

    @POST(Endpoint.Google.OAUTH_V4_TOKEN)
    fun signOAuth(@Body request: GoogleOAuthRequest): Single<GoogleOAuthResponse>
}
