package com.civilcam.domain.usecase.docs

import com.civilcam.data.repository.PublicDocsRepository

class GetTermsLinksUseCase(
    private val publicDocsRepository: PublicDocsRepository
) {
    suspend fun invoke() = publicDocsRepository.getLegalDocs()
}