package com.civilcam.data.network.support

import com.civilcam.domainLayer.repos.AccountRepository
import com.civilcam.utils.LocaleHelper
import okhttp3.Interceptor
import okhttp3.Response

class UserLanguageInterceptor(
    private val accountRepository: AccountRepository,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .newBuilder()
            .addHeader(HEADER_LANGUAGE, LocaleHelper.getSelectedLanguage().langValue)
            .addHeader(HEADER_DEVICE, "android")
            .addHeader(HEADER_SESSION_ID, accountRepository.deviceIdToken)
            .build()
            .let(chain::proceed)
    }

    companion object {
        private const val HEADER_LANGUAGE = "language"
        private const val HEADER_DEVICE = "x-device-os"
        private const val HEADER_SESSION_ID = "x-device-id"

    }
}
