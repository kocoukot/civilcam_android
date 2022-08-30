package com.civilcam.di

import com.civilcam.BuildConfig
import com.civilcam.data.ext.RetrofitConverterFactory
import com.civilcam.data.network.Path
import com.civilcam.data.network.service.*
import com.civilcam.data.network.support.ApiKeyInterceptor
import com.civilcam.data.network.support.SessionIdInterceptor
import com.civilcam.data.network.support.UserLanguageInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.parameter.ParametersHolder
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

private const val API_CONNECT_TIMEOUT = 15L
private const val API_READ_WRITE_TIMEOUT = 30L

val networkModule = module {

    single {
        GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create()
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(SessionIdInterceptor(get()))
            .addInterceptor(UserLanguageInterceptor(get()))
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .connectTimeout(API_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(API_READ_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(API_READ_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    factory { parameters: ParametersHolder ->
        val path = parameters.getOrNull<String>()
        Retrofit.Builder()
            .client(get())
            .baseUrl("${BuildConfig.API_GATEWAY}/$path/")
            .addConverterFactory(RetrofitConverterFactory(get()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .build()
    }

    single(GoogleRetrofit) {
        get<Retrofit>()
            .newBuilder()
            .baseUrl("${BuildConfig.GOOGLE_API_GATEWAY}/")
            .build()
    }

    single {
        get<Retrofit> { parametersOf(Path.AUTH) }
            .create(AuthService::class.java)
    }

    single {
        get<Retrofit> { parametersOf(Path.PROFILE) }
            .create(ProfileService::class.java)
    }

    single {
        get<Retrofit> { parametersOf(Path.PUBLIC) }
            .create(PublicService::class.java)
    }

    single {
        get<Retrofit> { parametersOf(Path.SUBSCRIPTIONS) }
            .create(SubscriptionsService::class.java)
    }

    single {
        get<Retrofit> { parametersOf(Path.USER) }
            .create(UserService::class.java)
    }

    single {
        get<Retrofit> { parametersOf(Path.VERIFICATION) }
            .create(VerificationService::class.java)
    }

    single {
        get<Retrofit> { parametersOf(Path.GUARDIANS) }
            .create(GuardiansService::class.java)
    }

    single {
        get<Retrofit>(GoogleRetrofit)
            .create(GoogleOAuthService::class.java)
    }
}