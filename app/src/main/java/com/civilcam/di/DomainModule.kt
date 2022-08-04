package com.civilcam.di

import com.civilcam.domain.usecase.GetUserInformationUseCase
import com.civilcam.domain.usecase.alerts.GetAlertsListUseCase
import com.civilcam.domain.usecase.alerts.GetHistoryAlertListUseCase
import com.civilcam.domain.usecase.alerts.GetHistoryDetailUseCase
import com.civilcam.domain.usecase.auth.SingUpUseCase
import com.civilcam.domain.usecase.docs.GetTermsLinksUseCase
import com.civilcam.domain.usecase.guardians.GetGuardsListUseCase
import com.civilcam.domain.usecase.guardians.GetGuardsRequestsUseCase
import com.civilcam.domain.usecase.guardians.SearchGuardsResultUseCase
import com.civilcam.domain.usecase.location.GetPlacesAutocompleteUseCase
import com.civilcam.domain.usecase.profile.GetUserProfileUseCase
import com.civilcam.domain.usecase.profile.SetAvatarUseCase
import com.civilcam.domain.usecase.profile.SetPersonalInfoUseCase
import com.civilcam.domain.usecase.settings.CheckCurrentPasswordUseCase
import com.civilcam.domain.usecase.settings.GetCurrentSubscriptionPlanUseCase
import com.civilcam.domain.usecase.user.AcceptLegalDocsUseCase
import com.civilcam.domain.usecase.verify.SendOtpCodeUseCase
import com.civilcam.domain.usecase.verify.VerifyEmailOtpUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { SingUpUseCase(get(), get()) }

    factory { AcceptLegalDocsUseCase(get()) }

    factory { GetTermsLinksUseCase(get()) }

    factory { SetPersonalInfoUseCase(get()) }

    factory { SetAvatarUseCase(get()) }

    factory { VerifyEmailOtpUseCase(get()) }

    factory { SendOtpCodeUseCase(get()) }

    factory { GetUserProfileUseCase(get()) }



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