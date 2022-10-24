package com.civilcam.ui.network.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ui.network.contacts.model.ContactsRoute
import org.koin.androidx.viewmodel.ext.android.viewModel


class ContactsFragment : Fragment() {
    private val viewModel: ContactsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                ContactsRoute.GoBack -> navController.popBackStack()
                ContactsRoute.GoInviteByNumber -> navController.navigate(R.id.action_contactsFragment_to_inviteByNumberFragment)
            }
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    viewLifecycleOwner
                )
            )

            setContent {
                ContactsScreenContent(viewModel)
            }
        }
    }

}