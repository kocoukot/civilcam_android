package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.PaginationRequest
import com.civilcam.domainLayer.model.guard.GuardianModel

interface GuardiansRepository {

    suspend fun searchGuardian(
        query: String, page: PaginationRequest.Pagination
    ): List<GuardianModel>

    suspend fun inviteByNumber(phoneNumber: String): Boolean

    suspend fun askToGuard(personId: Int): Boolean

}