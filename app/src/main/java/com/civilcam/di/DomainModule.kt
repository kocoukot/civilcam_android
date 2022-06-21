package com.civilcam.di

import com.civilcam.domain.usecase.GetUserInformationUseCase
import org.koin.dsl.module

val domainModule = module {

    factory { GetUserInformationUseCase(get()) }

}