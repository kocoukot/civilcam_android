package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.model.PaginationRequest
import com.civilcam.domainLayer.model.guard.NetworkType
import com.civilcam.domainLayer.model.guard.PersonModel
import com.civilcam.domainLayer.model.guard.UserInviteModel
import com.civilcam.domainLayer.model.guard.UserNetworkModel

interface GuardiansRepository {

    suspend fun getUserNetwork(networkType: NetworkType): UserNetworkModel

    suspend fun searchGuardian(
        query: String, page: PaginationRequest.Pagination
    ): List<PersonModel>

    suspend fun inviteByNumber(phoneNumber: String): Boolean

    suspend fun askToGuard(personId: Int): Boolean

    suspend fun getPersonDetail(personId: Int): PersonModel

    suspend fun setRequestReaction(reaction: ButtonAnswer, requestId: Int): Boolean

    suspend fun getInvitesList(): List<UserInviteModel>

    suspend fun stopGuarding(personId: Int): Boolean

    suspend fun deleteGuardian(personId: Int): Boolean

}