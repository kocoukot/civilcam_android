package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage
import com.civilcam.data.mapper.guardian.SearchGuardianListMapper
import com.civilcam.data.network.model.request.guardians.InviteByPhoneRequest
import com.civilcam.data.network.model.request.guardians.SearchGuardiansRequest
import com.civilcam.data.network.service.GuardiansService
import com.civilcam.data.network.support.BaseRepository
import com.civilcam.data.network.support.Resource
import com.civilcam.domainLayer.model.PaginationRequest
import com.civilcam.domainLayer.model.guard.GuardianModel
import com.civilcam.domainLayer.repos.GuardiansRepository

class GuardiansRepositoryImpl(
    private val guardiansService: GuardiansService,
    private val accountStorage: AccountStorage,
) : GuardiansRepository, BaseRepository() {

    private val guardianSearchMapper = SearchGuardianListMapper()

    override suspend fun searchGuardian(
        query: String,
        page: PaginationRequest.Pagination
    ): List<GuardianModel> =
        safeApiCall {
            guardiansService.searchGuardians(
                SearchGuardiansRequest(
                    pageInfo = page,
                    search = query
                )
            )
        }.let { response ->
            when (response) {
                is Resource.Success -> guardianSearchMapper.mapData(response.value.list)
                is Resource.Failure -> {
                    if (response.serviceException.isForceLogout) accountStorage.logOut()
                    throw response.serviceException
                }
            }
        }

    override suspend fun inviteByNumber(phoneNumber: String): Boolean =
        safeApiCall {
            guardiansService.inviteByPhone(InviteByPhoneRequest(phoneNumber))
        }.let { response ->
            when (response) {
                is Resource.Success -> response.value.ok
                is Resource.Failure -> {
                    if (response.serviceException.isForceLogout) accountStorage.logOut()
                    throw response.serviceException
                }
            }
        }
}
