package com.civilcam.di

import com.civilcam.data.repository.*
import org.koin.dsl.module

val repositoryModule = module {

    factory { MockRepository() }

    factory<PlacesRepository> { PlacesRepositoryImpl() }

    factory<AccountRepository> { AccountRepositoryImpl(get()) }

    factory<VerificationRepository> { VerificationRepositoryImpl(get()) }

    factory<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    factory<ProfileRepository> { ProfileRepositoryImpl(get(), get(), get()) }

    factory<PublicDocsRepository> { PublicDocsRepositoryImpl(get()) }

    factory<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
}