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
import com.civilcam.domain.usecase.user.LogoutUseCase
import com.civilcam.domain.usecase.verify.SendOtpCodeUseCase
import com.civilcam.domain.usecase.verify.VerifyEmailOtpUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { com.civilcam.domainLayer.usecase.auth.SingUpUseCase(get(), get()) }

    factory { com.civilcam.domainLayer.usecase.user.AcceptLegalDocsUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.docs.GetTermsLinksUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.profile.SetPersonalInfoUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.profile.SetAvatarUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.verify.VerifyEmailOtpUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.verify.SendOtpCodeUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.profile.GetUserProfileUseCase(get()) }

    factory { LogoutUseCase(get()) }

    factory { GetCurrentUserUseCase(get()) }

    factory { ChangePhoneNumberUseCase(get()) }

    factory { UpdateUserProfileUseCase(get()) }

    factory { GetUserInformationUseCase(get()) }
    factory { com.civilcam.domainLayer.usecase.user.LogoutUseCase(get()) }


    factory { com.civilcam.domainLayer.usecase.GetUserInformationUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.alerts.GetAlertsListUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.alerts.GetHistoryAlertListUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.alerts.GetHistoryDetailUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.settings.CheckCurrentPasswordUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.guardians.GetGuardsListUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.guardians.GetGuardsRequestsUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.guardians.SearchGuardsResultUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.settings.GetCurrentSubscriptionPlanUseCase(get()) }

    factory { com.civilcam.domainLayer.usecase.location.GetPlacesAutocompleteUseCase(get()) }

}