package com.civilcam.di

import com.civilcam.domain.model.VerificationFlow
import com.civilcam.ui.alerts.details.AlertsDetailViewModel
import com.civilcam.ui.alerts.history.AlertsHistoryViewModel
import com.civilcam.ui.alerts.list.AlertsListViewModel
import com.civilcam.ui.auth.create.CreateAccountViewModel
import com.civilcam.ui.auth.login.LoginViewModel
import com.civilcam.ui.auth.password.create.CreatePasswordViewModel
import com.civilcam.ui.auth.password.reset.ResetPasswordViewModel
import com.civilcam.ui.langSelect.LanguageSelectViewModel
import com.civilcam.ui.network.contacts.ContactsViewModel
import com.civilcam.ui.network.inviteByNumber.InviteByNumberViewModel
import com.civilcam.ui.network.main.NetworkMainViewModel
import com.civilcam.ui.onboarding.OnBoardingViewModel
import com.civilcam.ui.profile.setup.ProfileSetupViewModel
import com.civilcam.ui.profile.userDetails.UserDetailsViewModel
import com.civilcam.ui.settings.SettingsViewModel
import com.civilcam.ui.terms.TermsViewModel
import com.civilcam.ui.verification.VerificationViewModel
import com.google.android.libraries.places.api.Places
import com.standartmedia.di.source.GlobalKoinInjector
import com.standartmedia.di.source.KoinInjector
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val baseModule = module {

	single<KoinInjector> { GlobalKoinInjector(getKoin()) }

	single { Places.createClient(get()) }

}

val authModule = module {
	viewModel { LanguageSelectViewModel() }

	viewModel { OnBoardingViewModel() }

	viewModel { (isSettings: Boolean) -> TermsViewModel(isSettings) }

	viewModel { ProfileSetupViewModel(get()) }

	viewModel { LoginViewModel() }

	viewModel { CreateAccountViewModel() }

	viewModel { (verificationFlow: VerificationFlow, verificationSubject: String) ->
		VerificationViewModel(
			verificationFlow,
			verificationSubject
		)
	}

	viewModel { ResetPasswordViewModel() }

	viewModel { CreatePasswordViewModel() }


}

val networkRootModule = module {

    viewModel { UserDetailsViewModel(get()) }

    viewModel { ContactsViewModel(get()) }

    viewModel { InviteByNumberViewModel() }

    viewModel { NetworkMainViewModel() }


}

val alertsRootModule = module {

	viewModel { AlertsListViewModel(get()) }

	viewModel { AlertsHistoryViewModel(get()) }

	viewModel { AlertsDetailViewModel(get()) }


}

val profileModule = module {

	viewModel { SettingsViewModel() }

}


val presentationModules = arrayOf(
	baseModule,
	authModule,
	networkRootModule,
	alertsRootModule,
	profileModule
)