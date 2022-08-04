package com.civilcam.data.repository

import com.civilcam.domain.model.docs.LegalDocs

interface PublicDocsRepository {

    suspend fun getLegalDocs(): LegalDocs
}