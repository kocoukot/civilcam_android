package com.civilcam.di

import com.civilcam.ui.langSelect.LanguageSelectViewModel
import com.civilcam.ui.onboarding.OnBoardingViewModel
import com.civilcam.ui.terms.TermsViewModel
import com.google.android.libraries.places.api.Places
import com.standartmedia.di.source.GlobalKoinInjector
import com.standartmedia.di.source.KoinInjector
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val baseModule = module {

    single<KoinInjector> { GlobalKoinInjector(getKoin()) }

    single { Places.createClient(get()) }

//    viewModel { OnBoardingViewModel() }

}

val authModule = module {
    viewModel { LanguageSelectViewModel() }

    viewModel { OnBoardingViewModel() }

    viewModel { (isSettings: Boolean) -> TermsViewModel(isSettings) }
}

val presentationModules = arrayOf(
    baseModule,
    authModule,
)