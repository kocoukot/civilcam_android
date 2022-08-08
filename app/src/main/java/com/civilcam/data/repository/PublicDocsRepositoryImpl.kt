package com.civilcam.data.repository

import com.civilcam.common.ext.BaseRepository
import com.civilcam.common.ext.Resource
import com.civilcam.data.network.service.PublicService

class PublicDocsRepositoryImpl(
    private val publicService: PublicService,
) : com.civilcam.domainLayer.repos.PublicDocsRepository, BaseRepository() {


    override suspend fun getLegalDocs(): com.civilcam.domainLayer.model.docs.LegalDocs =
        safeApiCall {
            publicService.legalDocs()
        }.let { response ->
            when (response) {
                is Resource.Success -> {
                    com.civilcam.domainLayer.model.docs.LegalDocs(
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