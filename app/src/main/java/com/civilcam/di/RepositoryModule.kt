package com.civilcam.di

import com.civilcam.data.repository.*
import org.koin.dsl.module

val repositoryModule = module {

    factory { MockRepository() }

    factory<PlacesRepository> { PlacesRepositoryImpl() }

    factory<AccountRepository> { AccountRepositoryImpl(get()) }


}