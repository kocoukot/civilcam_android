package com.civilcam.data.network.service

import com.civilcam.data.network.Endpoint
import com.civilcam.data.network.model.response.publicDocs.LegalDocsResponse
import retrofit2.http.GET

interface PublicService {
    @GET(Endpoint.Public.LEGAL_DOCS)
    suspend fun legalDocs(): LegalDocsResponse
}