package com.civilcam.ui.network.contacts

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.registerForPermissionsResult
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
                ContactsRoute.GoInviteByNumber -> {}
            }
        }

        if (contactsPermissionDelegate.checkSelfPermissions()) {
            viewModel.fetchContacts()
        } else {
            pendingAction = { viewModel.fetchContacts() }
            contactsPermissionDelegate.requestPermissions()
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
}