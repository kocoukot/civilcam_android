package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage
import com.civilcam.data.mapper.guardian.InviteMapper
import com.civilcam.data.mapper.guardian.SearchGuardianListMapper
import com.civilcam.data.mapper.guardian.SearchGuardianMapper
import com.civilcam.data.network.model.request.guardians.*
import com.civilcam.data.network.service.GuardiansService
import com.civilcam.data.network.support.BaseRepository
import com.civilcam.data.network.support.Resource
import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.model.PaginationRequest
import com.civilcam.domainLayer.model.guard.PersonModel
import com.civilcam.domainLayer.model.guard.UserInviteModel
import com.civilcam.domainLayer.repos.GuardiansRepository

class GuardiansRepositoryImpl(
    private val guardiansService: GuardiansService,
    private val accountStorage: AccountStorage,
) : GuardiansRepository, BaseRepository() {

    private val guardianSearchMapper = SearchGuardianListMapper()
    private val personMapper = SearchGuardianMapper()
    private val inviteMapper = InviteMapper()

    override suspend fun searchGuardian(
        query: String,
        page: PaginationRequest.Pagination
    ): List<PersonModel> =
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
                    response.checkIfLogOut { accountStorage.logOut() }
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
                    response.checkIfLogOut { accountStorage.logOut() }
                    throw response.serviceException
                }
            }
        }

    override suspend fun askToGuard(personId: Int): Boolean =
        safeApiCall {
            guardiansService.askToGuard(AskToGuardRequest(personId = personId))
        }.let { response ->
            when (response) {
                is Resource.Success -> response.value.ok
                is Resource.Failure -> {
                    response.checkIfLogOut { accountStorage.logOut() }
                    throw response.serviceException
                }
            }
        }

    override suspend fun getPersonDetail(personId: Int): PersonModel =
        safeApiCall {
            guardiansService.getPersonDetail(PersonIdRequest(personId = personId))
        }.let { response ->
            when (response) {
                is Resource.Success -> personMapper.mapData(response.value.person)
                is Resource.Failure -> {
                    response.checkIfLogOut { accountStorage.logOut() }
                    throw response.serviceException
                }
            }
        }

    override suspend fun setRequestReaction(reaction: ButtonAnswer, personId: Int): Boolean =
        safeApiCall {
            guardiansService.setRequestReaction(
                RequestReactionRequest(reaction = reaction.domain, id = personId)
            )
        }.let { response ->
            when (response) {
                is Resource.Success -> true
                is Resource.Failure -> {
                    response.checkIfLogOut { accountStorage.logOut() }
                    throw response.serviceException
                }
            }
        }

    override suspend fun getInvitesList(): List<UserInviteModel> =
        safeApiCall {
            guardiansService.invitesList()
        }.let { response ->
            when (response) {
                is Resource.Success -> response.value.list.map { inviteMapper.mapData(it) }
                is Resource.Failure -> {
                    response.checkIfLogOut { accountStorage.logOut() }
                    throw response.serviceException
                }
            }
        }
}
