package com.civilcam.data.repository

import com.civilcam.common.ext.BaseRepository
import com.civilcam.common.ext.Resource
import com.civilcam.data.network.service.PublicService
import com.civilcam.domainLayer.model.docs.LegalDocs
import com.civilcam.domainLayer.repos.PublicDocsRepository

class PublicDocsRepositoryImpl(
    private val publicService: PublicService,
) : PublicDocsRepository, BaseRepository() {


    override suspend fun getLegalDocs(): LegalDocs =
        safeApiCall {
            publicService.legalDocs()
        }.let { response ->
            when (response) {
                is Resource.Success -> {
                    LegalDocs(
                        response.value.termsAndConditions,
                        response.value.privacyPolicy
                    )
                }
                is Resource.Failure -> {
                    throw response.serviceException
                }
            }
        }
}