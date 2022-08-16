package com.civilcam.di

import com.civilcam.data.repository.*
import com.civilcam.domainLayer.repos.*
import org.koin.dsl.module

val repositoryModule = module {

    factory<MockRepository> { MockRepositoryImpl() }

    factory<PlacesRepository> { PlacesRepositoryImpl() }

    factory<AccountRepository> { AccountRepositoryImpl(get()) }

    factory<VerificationRepository> { VerificationRepositoryImpl(get()) }

    factory<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    factory<ProfileRepository> { ProfileRepositoryImpl(get(), get(), get()) }

    factory<PublicDocsRepository> { PublicDocsRepositoryImpl(get()) }

    factory<UserRepository> { UserRepositoryImpl(get(), get(), get()) }

    factory<LocationRepository> { LocationRepositoryImpl(get()) }

}