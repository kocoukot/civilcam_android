package com.civilcam.di

import com.civilcam.domain.usecase.GetUserInformationUseCase
import com.civilcam.domain.usecase.alerts.GetAlertsListUseCase
import com.civilcam.domain.usecase.alerts.GetHistoryAlertListUseCase
import org.koin.dsl.module

val domainModule = module {

    factory { GetUserInformationUseCase(get()) }

    factory { GetAlertsListUseCase(get()) }

    factory { GetHistoryAlertListUseCase(get()) }
}