package com.civilcam.ui.network.contacts

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
import com.civilcam.common.ext.clearPhone
import com.civilcam.common.theme.CCTheme
import com.civilcam.domainLayer.model.AlertDialogTypes
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.common.compose.*
import com.civilcam.ui.common.compose.inputs.SearchInputField
import com.civilcam.ui.common.loading.DialogLoadingContent
import com.civilcam.ui.network.contacts.model.ContactsActions
import com.civilcam.ui.network.contacts.model.LetterContactItem
import com.civilcam.ui.network.contacts.model.PersonContactItem

@Composable
fun ContactsScreenContent(viewModel: ContactsViewModel) {

    val state = viewModel.state.collectAsState()

    if (state.value.isLoading) DialogLoadingContent()

    if (state.value.errorText.isNotEmpty()) {
        AlertDialogComp(
            dialogText = state.value.errorText,
            alertType = AlertDialogTypes.OK,
            onOptionSelected = { viewModel.setInputActions(ContactsActions.ClearErrorText) }
        )
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
               if (state.value.data?.contactsList.isNullOrEmpty()) RowDivider()
            }
        }) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {


            state.value.data?.contactsList?.let { data ->
                LazyColumn {
                    itemsIndexed(data) { index, contact ->
                        when (contact) {
                            is LetterContactItem -> Column {
                                HeaderTitleText(contact.letter)
                            }
                            is PersonContactItem -> {
                                var isInvited by remember { mutableStateOf(contact.isInvited) }
                                isInvited =
                                    contact.isInvited || contact.phoneNumber.clearPhone() in state.value.invitesList.map { it.phone.clearPhone() }
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
                                        if (isInvited) {
                                            Text(
                                                stringResource(id = R.string.invite_sent_text),
                                                textAlign = TextAlign.End,
                                                style = CCTheme.typography.common_text_medium,
                                                modifier = Modifier.padding(end = 16.dp),
                                                color = CCTheme.colors.grayOne
                                            )
                                        } else {
                                            TextActionButton(actionTitle = stringResource(id = R.string.invite_text)) {
                                                isInvited = true
                                                viewModel.setInputActions(
                                                    ContactsActions.ClickInvite(contact)
                                                )
                                            }
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