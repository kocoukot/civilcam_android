package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.model.PaginationRequest
import com.civilcam.domainLayer.model.guard.*

interface GuardiansRepository {

    suspend fun getUserNetwork(networkType: NetworkType): UserNetworkModel

    suspend fun searchGuardian(
        query: String, page: PaginationRequest.Pagination
    ): List<PersonModel>

    suspend fun getUserRequest(): List<GuardianItem>

    suspend fun inviteByNumber(phoneNumber: String): Boolean

    suspend fun askToGuard(personId: Int): PersonModel

    suspend fun getPersonDetail(personId: Int): PersonModel

    suspend fun setRequestReaction(reaction: ButtonAnswer, requestId: Int): PersonModel

    suspend fun getInvitesList(): List<UserInviteModel>

    suspend fun stopGuarding(personId: Int): PersonModel

    suspend fun deleteGuardian(personId: Int): PersonModel

}