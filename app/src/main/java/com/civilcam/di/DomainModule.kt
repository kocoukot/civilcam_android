package com.civilcam.di

import com.civilcam.domainLayer.usecase.GetUserInformationUseCase
import com.civilcam.domainLayer.usecase.alerts.GetAlertsListUseCase
import com.civilcam.domainLayer.usecase.alerts.GetHistoryAlertListUseCase
import com.civilcam.domainLayer.usecase.alerts.GetHistoryDetailUseCase
import com.civilcam.domainLayer.usecase.auth.CheckEmailUseCase
import com.civilcam.domainLayer.usecase.auth.SignInUseCase
import com.civilcam.domainLayer.usecase.auth.SingUpUseCase
import com.civilcam.domainLayer.usecase.docs.GetTermsLinksUseCase
import com.civilcam.domainLayer.usecase.guardians.GetGuardsListUseCase
import com.civilcam.domainLayer.usecase.guardians.GetGuardsRequestsUseCase
import com.civilcam.domainLayer.usecase.guardians.SearchGuardsResultUseCase
import com.civilcam.domainLayer.usecase.location.GetPlacesAutocompleteUseCase
import com.civilcam.domainLayer.usecase.profile.*
import com.civilcam.domainLayer.usecase.settings.CheckCurrentPasswordUseCase
import com.civilcam.domainLayer.usecase.settings.GetCurrentSubscriptionPlanUseCase
import com.civilcam.domainLayer.usecase.user.AcceptLegalDocsUseCase
import com.civilcam.domainLayer.usecase.user.GetCurrentUserUseCase
import com.civilcam.domainLayer.usecase.user.LogoutUseCase
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
	
	factory { GetUserInformationUseCase(get()) }
	
	factory { GetCurrentUserUseCase(get()) }
	
	factory { ChangePhoneNumberUseCase(get()) }
	
	factory { UpdateUserProfileUseCase(get()) }
	
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


















