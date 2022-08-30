package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage
import com.civilcam.data.network.model.request.guardians.SearchGuardiansRequest
import com.civilcam.data.network.service.GuardiansService
import com.civilcam.data.network.support.BaseRepository
import com.civilcam.data.network.support.Resource
import com.civilcam.domainLayer.model.PaginationRequest
import com.civilcam.domainLayer.model.guard.GuardianModel
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.domainLayer.model.user.ImageInfo
import com.civilcam.domainLayer.repos.GuardiansRepository

class GuardiansRepositoryImpl(
    private val guardiansService: GuardiansService,
    private val accountStorage: AccountStorage,
) : GuardiansRepository, BaseRepository() {


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
                is Resource.Success -> {
                    response.value.list.map {
                        GuardianModel(
                            guardianId = it.id,
                            guardianName = it.fullName,
                            guardianAvatar = it.avatar?.let { avatar ->
                                ImageInfo(
                                    mimetype = avatar.imageMimetype,
                                    imageUrl = avatar.imageUrl,
                                    imageKey = avatar.imageKey,
                                    thumbnailKey = avatar.thumbnailKey,
                                    thumbnailUrl = avatar.thumbnailUrl,
                                )
                            },
                            guardianStatus = GuardianStatus.NEW,
                        )
                    }
                }
                is Resource.Failure -> {
                    if (response.serviceException.isForceLogout) accountStorage.logOut()
                    throw response.serviceException
                }
            }
        }
}
