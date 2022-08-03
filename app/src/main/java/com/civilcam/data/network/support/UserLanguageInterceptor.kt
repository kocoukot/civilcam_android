package com.civilcam.data.network.support

import com.civilcam.data.repository.AccountRepository
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
            .build()
            .let(chain::proceed)
    }

    companion object {
        private const val HEADER_LANGUAGE = "language"
    }
}
