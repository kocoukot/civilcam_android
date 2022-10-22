package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.docs.LegalDocs

interface PublicDocsRepository {
    suspend fun getLegalDocs(): LegalDocs
}