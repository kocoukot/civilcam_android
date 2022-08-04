package com.civilcam.data.network.support

import com.civilcam.data.repository.AccountRepository
import com.civilcam.utils.LocaleHelper
import okhttp3.Interceptor
import okhttp3.Response

class DeviceInfoInterceptor(
    private val accountRepository: AccountRepository,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .newBuilder()
            .addHeader(HEADER_LANGUAGE, LocaleHelper.getSelectedLanguage().langValue)
            .addHeader(HEADER_DEVICE, "android")
            .build()
            .let(chain::proceed)
    }

    companion object {
        private const val HEADER_LANGUAGE = "language"
        private const val HEADER_DEVICE = "x-device-os"
    }
}
