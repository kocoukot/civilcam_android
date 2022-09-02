package com.civilcam.data.network.service

import com.civilcam.data.network.Endpoint
import com.civilcam.data.network.model.request.guardians.*
import com.civilcam.data.network.model.response.SuccessResponse
import com.civilcam.data.network.model.response.guardians.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GuardiansService {

    @POST(Endpoint.Guardians.NETWORK)
    suspend fun userNetwork(@Body request: UserNetworkRequest): UserNetworkResponse

    @POST(Endpoint.Guardians.SEARCH)
    suspend fun searchGuardians(@Body request: SearchGuardiansRequest): SearchGuardiansResponse

    @GET(Endpoint.Guardians.INVITES)
    suspend fun invitesList(): InvitesListResponse

    @POST(Endpoint.Guardians.INVITE_BY_PHONE)
    suspend fun inviteByPhone(@Body request: InviteByPhoneRequest): SuccessResponse

    @GET(Endpoint.Guardians.REQUESTS)
    suspend fun userGuardRequests(): UserGuardRequestListResponse

    @POST(Endpoint.Guardians.PERSON)
    suspend fun getPersonDetail(@Body request: PersonDetailRequest): PersonResponse

    @POST(Endpoint.Guardians.ASK_TO_GUARD)
    suspend fun askToGuard(@Body request: AskToGuardRequest): SuccessResponse
}