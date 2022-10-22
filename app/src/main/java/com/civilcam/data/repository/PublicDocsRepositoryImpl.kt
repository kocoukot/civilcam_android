package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage
import com.civilcam.data.network.service.PublicService
import com.civilcam.data.network.support.BaseRepository
import com.civilcam.data.network.support.Resource
import com.civilcam.domainLayer.model.docs.LegalDocs
import com.civilcam.domainLayer.repos.PublicDocsRepository

class PublicDocsRepositoryImpl(
    private val publicService: PublicService,
    private val accountStorage: AccountStorage,
) : PublicDocsRepository, BaseRepository() {


    override suspend fun getLegalDocs(): LegalDocs =
        safeApiCall {
            publicService.legalDocs()
        }.let { response ->
            when (response) {
                is Resource.Success -> LegalDocs(
                    response.value.termsAndConditions,
                    response.value.privacyPolicy
                )
                is Resource.Failure -> {
                    response.checkIfLogOut { accountStorage.logOut() }
                    throw response.serviceException
                }
            }
        }
}