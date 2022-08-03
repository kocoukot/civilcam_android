package com.civilcam.di

import com.civilcam.domain.usecase.GetUserInformationUseCase
import com.civilcam.domain.usecase.alerts.GetAlertsListUseCase
import com.civilcam.domain.usecase.alerts.GetHistoryAlertListUseCase
import com.civilcam.domain.usecase.alerts.GetHistoryDetailUseCase
import com.civilcam.domain.usecase.auth.SingUpUseCase
import com.civilcam.domain.usecase.guardians.GetGuardsListUseCase
import com.civilcam.domain.usecase.guardians.GetGuardsRequestsUseCase
import com.civilcam.domain.usecase.guardians.SearchGuardsResultUseCase
import com.civilcam.domain.usecase.location.GetPlacesAutocompleteUseCase
import com.civilcam.domain.usecase.settings.CheckCurrentPasswordUseCase
import com.civilcam.domain.usecase.settings.GetCurrentSubscriptionPlanUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { SingUpUseCase(get()) }


    factory { GetUserInformationUseCase(get()) }

    factory { GetAlertsListUseCase(get()) }

    factory { GetHistoryAlertListUseCase(get()) }

    factory { GetHistoryDetailUseCase(get()) }

    factory { CheckCurrentPasswordUseCase(get()) }

    factory { GetGuardsListUseCase(get()) }

    factory { GetGuardsRequestsUseCase(get()) }

    factory { SearchGuardsResultUseCase(get()) }

    factory { GetCurrentSubscriptionPlanUseCase(get()) }

    factory { GetPlacesAutocompleteUseCase(get()) }
    
}