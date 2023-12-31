package com.civilcam.ui.network.contacts

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.ext_features.alert.AlertDialogButtons
import com.civilcam.ext_features.compose.elements.*
import com.civilcam.ext_features.ext.clearPhone
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.common.compose.inputs.SearchInputField
import com.civilcam.ui.network.contacts.model.ContactsActions
import com.civilcam.ui.network.contacts.model.InvitationState
import com.civilcam.ui.network.contacts.model.LetterContactItem
import com.civilcam.ui.network.contacts.model.PersonContactItem

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ContactsScreenContent(viewModel: ContactsViewModel) {

    val state = viewModel.state.collectAsState()

    if (state.value.isLoading) DialogLoadingContent()

    if (state.value.errorText.isNotEmpty()) {
        AlertDialogComp(
            dialogText = state.value.errorText,
            alertType = AlertDialogButtons.OK,
            onOptionSelected = { viewModel.setInputActions(ContactsActions.ClearErrorText) }
        )
    }

    val permissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { if (it) viewModel.queryContacts() })

    LaunchedEffect(key1 = true) {
        permissionRequest.launch(Manifest.permission.READ_CONTACTS)
    }

    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CCTheme.colors.white)
            ) {
                TopAppBarContent(
                    backgroundColor = CCTheme.colors.white,
                    title = stringResource(id = R.string.add_from_contacts_title),
                    navigationItem = {
                        BackButton {
                            viewModel.setInputActions(ContactsActions.ClickGoBack)
                        }
                    },
                    actionItem = {
                        TextActionButton(actionTitle = stringResource(id = R.string.add_from_contacts_invite_by)) {
                            viewModel.setInputActions(ContactsActions.ClickGoInviteByNumber)
                        }
                    }
                )
                SearchInputField(
                    onValueChanged = { viewModel.setInputActions(ContactsActions.ClickSearch(it)) },
                    isFocused = {},
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (state.value.contactsList.isNullOrEmpty()) RowDivider()
            }
        }) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            state.value.contactsList?.let { data ->
                LazyColumn {
                    itemsIndexed(data) { index, contact ->
                        when (contact) {
                            is LetterContactItem -> Column {
                                HeaderTitleText(contact.letter)
                            }
                            is PersonContactItem -> {
                                var isInvited by remember { mutableStateOf(contact.isInvited) }

                                isInvited =
                                    if (contact.isInvited == InvitationState.IN_APP) InvitationState.IN_APP
                                    else {
                                        if (contact.isInvited == InvitationState.PENDING || contact.phoneNumber.clearPhone() in state.value.invitesList.map { it.phone.clearPhone() }) {
                                            InvitationState.PENDING
                                        } else {
                                            InvitationState.NEW
                                        }
                                    }

                                InformationRow(
                                    needDivider = (if (index == data.lastIndex) {
                                        false
                                    } else {
                                        data[index + 1] is PersonContactItem
                                    }),
                                    title = contact.name,
                                    leadingIcon = {
                                        CircleUserAvatar(
                                            avatar = contact.avatar,
                                            avatarSize = 36
                                        )
                                    },
                                    trailingIcon = {
                                        when (isInvited) {
                                            InvitationState.NEW -> TextActionButton(
                                                actionTitle = stringResource(
                                                    id = R.string.invite_text
                                                )
                                            ) {
                                                isInvited = InvitationState.PENDING
                                                viewModel.setInputActions(
                                                    ContactsActions.ClickInvite(contact)
                                                )
                                            }
                                            InvitationState.PENDING -> Text(
                                                stringResource(id = R.string.invite_sent_text),
                                                textAlign = TextAlign.End,
                                                style = CCTheme.typography.common_text_medium,
                                                modifier = Modifier.padding(end = 16.dp),
                                                color = CCTheme.colors.grayOne
                                            )
                                            InvitationState.IN_APP -> {}
                                        }
                                    }
                                ) {}
                            }
                        }
                    }
                }
            }
        }
    }
}