package com.civilcam.ui.network.contacts

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import com.civilcam.ext_features.arch.BaseFragment
import com.civilcam.ext_features.requireArg
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ContactsFragment : BaseFragment<ContactsViewModel>() {

    private val numbersList by requireArg<List<String>>(ARG_IN_APP_NUMBERS)
    override val viewModel: ContactsViewModel by viewModel {
        parametersOf(numbersList)
    }

    override val screenContent: @Composable (ContactsViewModel) -> Unit = {
        ContactsScreenContent(viewModel)
    }

    companion object {
        private const val ARG_IN_APP_NUMBERS = "in_app_numbers"

        fun createArgs(numbersList: List<String>) = bundleOf(
            ARG_IN_APP_NUMBERS to numbersList
        )
    }
}