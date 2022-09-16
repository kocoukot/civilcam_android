package com.civilcam.ui.network.contacts

import android.Manifest
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
import com.civilcam.ext_features.registerForPermissionsResult
import com.civilcam.ui.network.contacts.model.ContactsRoute
import org.koin.androidx.viewmodel.ext.android.viewModel


class ContactsFragment : Fragment() {
    private val viewModel: ContactsViewModel by viewModel()

    private val contactsPermissionDelegate = registerForPermissionsResult(
        Manifest.permission.READ_CONTACTS
    ) { onContactsPermissionsGranted(it) }
    private var pendingAction: (() -> Unit)? = null

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

    private fun onContactsPermissionsGranted(isGranted: Boolean) {
        if (isGranted) {
            pendingAction?.invoke()
            pendingAction = null
        }
    }

    override fun onStart() {
        super.onStart()
        if (contactsPermissionDelegate.checkSelfPermissions()) {
            viewModel.fetchContacts()
        } else {
            pendingAction = { viewModel.fetchContacts() }
            contactsPermissionDelegate.requestPermissions()
        }

    }
}