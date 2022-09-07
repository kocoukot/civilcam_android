package com.civilcam.di

import com.civilcam.di.source.GlobalKoinInjector
import com.civilcam.di.source.KoinInjector
import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.ui.alerts.history.AlertsHistoryViewModel
import com.civilcam.ui.alerts.list.AlertsListViewModel
import com.civilcam.ui.alerts.map.LiveMapViewModel
import com.civilcam.ui.auth.create.CreateAccountViewModel
import com.civilcam.ui.auth.login.LoginViewModel
import com.civilcam.ui.auth.password.create.CreatePasswordViewModel
import com.civilcam.ui.auth.password.reset.ResetPasswordViewModel
import com.civilcam.ui.auth.pincode.PinCodeViewModel
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.emergency.EmergencyViewModel
import com.civilcam.ui.langSelect.LanguageSelectViewModel
import com.civilcam.ui.network.contacts.ContactsViewModel
import com.civilcam.ui.network.inviteByNumber.InviteByNumberViewModel
import com.civilcam.ui.network.main.NetworkMainViewModel
import com.civilcam.ui.network.main.model.NetworkScreen
import com.civilcam.ui.onboarding.OnBoardingViewModel
import com.civilcam.ui.profile.credentials.ChangeCredentialsViewModel
import com.civilcam.ui.profile.setup.ProfileSetupViewModel
import com.civilcam.ui.profile.userDetails.UserDetailsViewModel
import com.civilcam.ui.profile.userProfile.UserProfileViewModel
import com.civilcam.ui.profile.userProfile.model.UserProfileType
import com.civilcam.ui.settings.SettingsViewModel
import com.civilcam.ui.splash.SplashViewModel
import com.civilcam.ui.subscription.SubscriptionViewModel
import com.civilcam.ui.terms.TermsViewModel
import com.civilcam.ui.verification.VerificationViewModel
import com.google.android.libraries.places.api.Places
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val baseModule = module {
	
	single<KoinInjector> { GlobalKoinInjector(getKoin()) }
	
	single { Places.createClient(get()) }
	
}

val authModule = module {
	viewModel { LanguageSelectViewModel() }
	
	viewModel { OnBoardingViewModel() }
	
	viewModel { SplashViewModel(get(), get()) }
	
	viewModel { (isSettings: Boolean) -> TermsViewModel(isSettings, get(), get()) }
	
	viewModel { ProfileSetupViewModel(get(), get(), get(), get(), get()) }
	
	viewModel { LoginViewModel(get(), get()) }
	
	viewModel { CreateAccountViewModel(get(), get()) }
	
	viewModel { (verificationFlow: VerificationFlow, verificationSubject: String, newSubject: String) ->
		VerificationViewModel(
			verificationFlow,
			verificationSubject,
			newSubject,
			get(),
			get(),
			get()
		)
	}
	
	viewModel { ResetPasswordViewModel(get()) }
	
	viewModel { (token: String) -> CreatePasswordViewModel(token, get()) }
	
	viewModel { (pinCodeFlow: PinCodeFlow) -> PinCodeViewModel(pinCodeFlow, get(), get()) }
	
	viewModel { (isReselect: Boolean) -> SubscriptionViewModel(isReselect, get()) }
}


val networkRootModule = module {

	viewModel { (userId: Int) ->
		UserDetailsViewModel(
			userId, get(), get(), get(), get(),
			get(),
		)
	}

	viewModel { ContactsViewModel(get(), get(), get()) }

	viewModel { InviteByNumberViewModel(get(), get()) }

	viewModel { (screen: NetworkScreen) ->
		NetworkMainViewModel(
			get(),
			screen,
			get(),
			get(),
			get(),
			get(),
			get(),
			get(),
			get()
		)
    }
}

val alertsRootModule = module {
	
	viewModel { AlertsListViewModel(get()) }
	
	viewModel { AlertsHistoryViewModel(get()) }
	
	viewModel { (userId: Int) -> LiveMapViewModel(userId, get(), get()) }
}

val profileModule = module {
	
	viewModel {
		SettingsViewModel(
			get(),
			get(),
			get(),
			get(),
			get(),
			get(),
			get(),
			get(),
			get()
		)
	}
	
	viewModel { (credentialType: UserProfileType, credential: String) ->
		ChangeCredentialsViewModel(
			credentialType,
			credential,
			get(),
			get(),
		)
	}
	
	viewModel { UserProfileViewModel(get(), get(), get(), get(), get()) }
}

val emergencyModule = module {
	viewModel { EmergencyViewModel(get()) }
}


val presentationModules = arrayOf(
	baseModule,
	authModule,
	networkRootModule,
	alertsRootModule,
	profileModule,
	emergencyModule
)