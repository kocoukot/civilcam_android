package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage
import com.civilcam.data.mapper.guardian.InviteMapper
import com.civilcam.data.mapper.guardian.SearchGuardianListMapper
import com.civilcam.data.mapper.guardian.SearchGuardianMapper
import com.civilcam.data.mapper.guardian.UserNetworkMapper
import com.civilcam.data.network.model.request.guardians.*
import com.civilcam.data.network.service.GuardiansService
import com.civilcam.data.network.support.BaseRepository
import com.civilcam.data.network.support.Resource
import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.model.PaginationRequest
import com.civilcam.domainLayer.model.guard.NetworkType
import com.civilcam.domainLayer.model.guard.PersonModel
import com.civilcam.domainLayer.model.guard.UserInviteModel
import com.civilcam.domainLayer.model.guard.UserNetworkModel
import com.civilcam.domainLayer.repos.GuardiansRepository

class GuardiansRepositoryImpl(
    private val guardiansService: GuardiansService,
    private val accountStorage: AccountStorage,
) : GuardiansRepository, BaseRepository() {

    private val guardianSearchMapper = SearchGuardianListMapper()
    private val personMapper = SearchGuardianMapper()
    private val inviteMapper = InviteMapper()
    private val userNetworkMapper = UserNetworkMapper()

    override suspend fun getUserNetwork(networkType: NetworkType): UserNetworkModel =
        safeApiCall {
            guardiansService.userNetwork(
                UserNetworkRequest(
                    pageInfo = PaginationRequest.Pagination(1, 40),
                    networkType = networkType.domain
                )
            )
        }.let { response ->
            when (response) {
                is Resource.Success -> userNetworkMapper.mapData(response.value)
                is Resource.Failure -> {
                    response.checkIfLogOut { accountStorage.logOut() }
                    throw response.serviceException
                }
            }
        }

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

    override suspend fun setRequestReaction(reaction: ButtonAnswer, requestId: Int): Boolean =
        safeApiCall {
            guardiansService.setRequestReaction(
                RequestReactionRequest(reaction = reaction.domain, id = requestId)
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

    override suspend fun deleteGuardian(personId: Int): Boolean =
        safeApiCall {
            guardiansService.deleteGuardian(PersonIdRequest(personId))
        }.let { response ->
            when (response) {
                is Resource.Success -> true
                is Resource.Failure -> {
                    response.checkIfLogOut { accountStorage.logOut() }
                    throw response.serviceException
                }
            }
        }

    override suspend fun stopGuarding(personId: Int): Boolean =
        safeApiCall {
            guardiansService.stopGuarding(PersonIdRequest(personId))
        }.let { response ->
            when (response) {
                is Resource.Success -> true
                is Resource.Failure -> {
                    response.checkIfLogOut { accountStorage.logOut() }
                    throw response.serviceException
                }
            }
        }
}
