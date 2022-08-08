package com.civilcam.domainLayer.usecase.docs

import com.civilcam.domainLayer.repos.PublicDocsRepository

class GetTermsLinksUseCase(
    private val publicDocsRepository: PublicDocsRepository
) {
    suspend fun invoke() = publicDocsRepository.getLegalDocs()
}