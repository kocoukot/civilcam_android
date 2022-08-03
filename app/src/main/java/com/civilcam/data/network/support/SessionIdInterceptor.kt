package com.civilcam.data.network.support

import com.civilcam.data.repository.AccountRepository
import okhttp3.Interceptor
import okhttp3.Response

class SessionIdInterceptor(
    private val accountRepository: AccountRepository,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .newBuilder()
            .addHeader(HEADER_SESSION_ID, accountRepository.sessionToken)
            .build()
            .let(chain::proceed)
    }

    companion object {
        private const val HEADER_SESSION_ID = "X-Session-Id"
    }
}
