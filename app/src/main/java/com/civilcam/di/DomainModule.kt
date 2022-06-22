package com.civilcam.di

import com.civilcam.domain.usecase.GetUserInformationUseCase
import com.civilcam.domain.usecase.alerts.GetAlertsListUseCase
import org.koin.dsl.module

val domainModule = module {

    factory { GetUserInformationUseCase(get()) }

    factory { GetAlertsListUseCase(get()) }

}