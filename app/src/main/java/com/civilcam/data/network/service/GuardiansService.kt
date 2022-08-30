package com.civilcam.data.network.service

import com.civilcam.data.network.Endpoint
import com.civilcam.data.network.model.request.guardians.SearchGuardiansRequest
import com.civilcam.data.network.model.response.guardians.SearchGuardiansResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GuardiansService {
    @POST(Endpoint.Guardians.SEARCH)
    suspend fun searchGuardians(@Body request: SearchGuardiansRequest): SearchGuardiansResponse
}