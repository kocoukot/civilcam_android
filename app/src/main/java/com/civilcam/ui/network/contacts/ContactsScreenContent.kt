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
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.*
import com.civilcam.ui.network.contacts.model.ContactsActions
import com.civilcam.ui.network.contacts.model.LetterContactItem
import com.civilcam.ui.network.contacts.model.PersonContactItem

@Composable
fun ContactsScreenContent(viewModel: ContactsViewModel) {

    val state = viewModel.state.collectAsState()


    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        topBar = {
            TopAppBarContent(
                title = stringResource(id = R.string.add_from_contacts_title),
                navigationAction = {
                    viewModel.setInputActions(ContactsActions.ClickGoBack)
                },
                actionTitle = stringResource(id = R.string.add_from_contacts_invite_by),
                actionAction = {
                    viewModel.setInputActions(ContactsActions.ClickGoInviteByNumber)
                }
            )
        }) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            //search field
            DividerLightGray()

            state.value.data?.contactsList?.let { data ->
                LazyColumn {
                    itemsIndexed(data) { index, contact ->
                        when (contact) {
                            is LetterContactItem -> Box {
                                Text(
                                    contact.letter,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(CCTheme.colors.lightGray)
                                        .padding(vertical = 8.dp, horizontal = 16.dp),
                                    style = CCTheme.typography.common_text_small_medium,
                                    color = CCTheme.colors.black
                                )
                            }


                            is PersonContactItem -> {
                                var isInvited by remember { mutableStateOf(contact.isInvited) }

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
                                                stringResource(id = R.string.pending_text),
                                                textAlign = TextAlign.End,
                                                style = CCTheme.typography.common_text_medium,
                                                modifier = Modifier.padding(end = 16.dp),
                                                color = CCTheme.colors.grayOne
                                            )
                                        } else {
                                            TextActionButton(actionTitle = stringResource(id = R.string.add_text)) {
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
 