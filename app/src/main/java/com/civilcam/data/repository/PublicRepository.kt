package com.civilcam.data.repository

import com.civilcam.domain.model.docs.LegalDocs

interface PublicRepository {

    suspend fun getLegalDocs(): LegalDocs
}