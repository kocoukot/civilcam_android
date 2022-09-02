package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.model.PaginationRequest
import com.civilcam.domainLayer.model.guard.PersonModel

interface GuardiansRepository {

    suspend fun searchGuardian(
        query: String, page: PaginationRequest.Pagination
    ): List<PersonModel>

    suspend fun inviteByNumber(phoneNumber: String): Boolean

    suspend fun askToGuard(personId: Int): Boolean

    suspend fun getPersonDetail(personId: Int): PersonModel

    suspend fun setRequestReaction(reaction: ButtonAnswer, personId: Int): Boolean

}