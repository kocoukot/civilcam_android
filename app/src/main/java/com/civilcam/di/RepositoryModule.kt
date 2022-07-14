package com.civilcam.di

import com.civilcam.data.repository.MockRepository
import com.civilcam.data.repository.PlacesRepository
import com.civilcam.data.repository.PlacesRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    factory { MockRepository() }

    factory<PlacesRepository> { PlacesRepositoryImpl() }


}