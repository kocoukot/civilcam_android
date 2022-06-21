package com.civilcam.di

import com.civilcam.data.repository.MockRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory { MockRepository() }

}