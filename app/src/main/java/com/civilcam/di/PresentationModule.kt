package com.civilcam.di

import com.civilcam.ui.alerts.list.AlertsListViewModel
import com.civilcam.ui.langSelect.LanguageSelectViewModel
import com.civilcam.ui.onboarding.OnBoardingViewModel
import com.civilcam.ui.profile.setup.ProfileSetupViewModel
import com.civilcam.ui.profile.userDetails.UserDetailsViewModel
import com.civilcam.ui.settings.SettingsViewModel
import com.civilcam.ui.terms.TermsViewModel
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


}

val networkRootModule = module {

    viewModel { UserDetailsViewModel(get()) }

}

val alertsRootModule = module {

    viewModel { AlertsListViewModel(get()) }

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