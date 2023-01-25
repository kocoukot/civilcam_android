package com.civilcam.di

import com.civilcam.data.repository.*
import com.civilcam.domainLayer.repos.*
import org.koin.dsl.module

val repositoryModule = module {

    factory<PlacesRepository> { PlacesRepositoryImpl(get()) }

    factory<AccountRepository> { AccountRepositoryImpl(get()) }

    factory<VerificationRepository> { VerificationRepositoryImpl(get(), get()) }

    factory<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }

    factory<ProfileRepository> { ProfileRepositoryImpl(get(), get(), get(), get()) }

    factory<PublicDocsRepository> { PublicDocsRepositoryImpl(get(), get()) }

    factory<UserRepository> { UserRepositoryImpl(get(), get(), get()) }

    factory<LocationRepository> { LocationRepositoryImpl(get()) }

    factory<SubscriptionsRepository> { SubscriptionsRepositoryImpl(get(), get()) }

    factory<GuardiansRepository> { GuardiansRepositoryImpl(get(), get()) }

    factory<AlertsRepository> { AlertsRepositoryImpl(get(), get()) }

    factory<VideoRepository> { VideoRepositoryImpl() }


}