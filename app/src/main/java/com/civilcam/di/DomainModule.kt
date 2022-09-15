package com.civilcam.di

import com.civilcam.domainLayer.usecase.GetUserDetailUseCase
import com.civilcam.domainLayer.usecase.alerts.*
import com.civilcam.domainLayer.usecase.auth.*
import com.civilcam.domainLayer.usecase.docs.GetTermsLinksUseCase
import com.civilcam.domainLayer.usecase.guardians.*
import com.civilcam.domainLayer.usecase.location.FetchUserLocationUseCase
import com.civilcam.domainLayer.usecase.location.GetLastKnownLocationUseCase
import com.civilcam.domainLayer.usecase.places.GetPlacesAutocompleteUseCase
import com.civilcam.domainLayer.usecase.profile.*
import com.civilcam.domainLayer.usecase.subscriptions.GetCurrentSubscriptionPlanUseCase
import com.civilcam.domainLayer.usecase.subscriptions.GetSubscriptionsUseCase
import com.civilcam.domainLayer.usecase.user.*
import com.civilcam.domainLayer.usecase.verify.SendOtpCodeUseCase
import com.civilcam.domainLayer.usecase.verify.VerifyEmailOtpUseCase
import org.koin.dsl.module

val domainModule = module {
	factory { SingUpUseCase(get(), get()) }

	factory { SignInUseCase(get(), get()) }

    factory { CheckEmailUseCase(get()) }

    factory { AcceptLegalDocsUseCase(get()) }

    factory { GetTermsLinksUseCase(get()) }

    factory { SetPersonalInfoUseCase(get()) }

    factory { SetAvatarUseCase(get()) }

    factory { VerifyEmailOtpUseCase(get()) }

    factory { SendOtpCodeUseCase(get()) }

    factory { GetUserProfileUseCase(get()) }

    factory { LogoutUseCase(get()) }

    factory { GetUserDetailUseCase(get()) }

    factory { GetCurrentUserUseCase(get()) }

    factory { ChangePhoneNumberUseCase(get()) }

    factory { UpdateUserProfileUseCase(get()) }

    factory { GetAlertsListUseCase(get()) }

    factory { GetHistoryAlertListUseCase(get()) }

    factory { GetAlertDetailUseCase(get()) }

    factory { SearchGuardsResultUseCase(get()) }

    factory { GetCurrentSubscriptionPlanUseCase(get()) }

    factory { GetPlacesAutocompleteUseCase(get()) }

    factory { FetchUserLocationUseCase(get()) }

    factory { GetLastKnownLocationUseCase(get()) }

    factory { ChangeEmailUseCase(get()) }

    factory { CheckCurrentPasswordUseCase(get()) }

    factory { ChangePasswordUseCase(get()) }

    factory { SetUserLanguageUseCase(get()) }

    factory { GetMapAlertUserDataUseCase(get()) }

	factory { GoogleSignInUseCase(get(), get()) }

	factory { ResetPasswordUseCase(get()) }

	factory { VerifyResetPasswordOtpUseCase(get()) }

	factory { RecoverPasswordUseCase(get()) }

	factory { SaveFcmTokenUseCase(get()) }

	factory { SetFCMTokenUseCase(get()) }

	factory { DeleteAccountUseCase(get()) }

	factory { GetLocalCurrentUserUseCase(get()) }

    factory { IsUserLoggedInUseCase(get()) }

    factory { ContactSupportUseCase(get()) }

    factory { ToggleSettingsUseCase(get()) }

    factory { GetSubscriptionsUseCase(get()) }

    factory { InviteByNumberUseCase(get()) }

    factory { AskToGuardUseCase(get()) }

    factory { SetRequestReactionUseCase(get()) }

    factory { GetPhoneInvitesUseCase(get()) }

    factory { CheckPinUseCase(get()) }

    factory { SetPinUseCase(get()) }

    factory { StopGuardingUseCase(get()) }

    factory { DeleteGuardianUseCase(get()) }

    factory { GetUserNetworkUseCase(get()) }

    factory { GetNetworkRequestsUseCase(get()) }

    factory { FacebookSignInUseCase(get(), get()) }

    factory { ResolveAlertUseCase(get()) }


}








