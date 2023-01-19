package com.civilcam.ui.network.main.content

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.civilcam.R
import com.civilcam.data.local.model.Contact
import com.civilcam.domainLayer.model.guard.GuardianItem
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.domainLayer.model.guard.PersonModel
import com.civilcam.ext_features.compose.elements.*
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.network.main.model.NetworkMainActions
import timber.log.Timber

@Composable
fun GuardianSearchContent(
    lazyData: LazyPagingItems<PersonModel>?,
    pendingList: List<Int>,
    contactsList: List<Contact>,
    searchPart: String,
    onSearchAction: (NetworkMainActions) -> Unit,
) {
    Timber.tag("networkSearch").i("lazyList ${lazyData?.itemSnapshotList}")

    Crossfade(
        targetState = (lazyData?.itemCount ?: 0) > 0 || contactsList.isNotEmpty()
    ) { targetState ->
        when (targetState) {
            false -> EmptySearchScreenState()
            true -> {
                SearchResults(
                    lazyData,
                    contactsList,
                    pendingList,
                    searchPart,
                    onRowAction = onSearchAction::invoke
                )
            }
        }
    }
}

@Composable
private fun EmptySearchScreenState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            stringResource(id = R.string.add_guardian_search_title),
            style = CCTheme.typography.common_medium_text_bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(id = R.string.add_guardian_search_text),
            style = CCTheme.typography.common_text_regular
        )
    }
}


@Composable
private fun SearchResults(
    results: LazyPagingItems<PersonModel>?,
    contactsList: List<Contact>,
    pendingList: List<Int>,
    searchPart: String,
    onRowAction: (NetworkMainActions) -> Unit,
) {
    Timber.tag("networkSearch").i("lazyList ${results?.itemCount}")

    LazyColumn {
        itemsIndexed(contactsList, key = { _, item -> item.contactId }) { index, contact ->
            var userStatus by remember { mutableStateOf(contact.status) }

            SearchRow(
                title = contact.name,
                needDivider = index < contactsList.lastIndex,
                leadingIcon = {
                    contact.avatar.let {
                        CircleUserAvatar(
                            avatar = it,
                            avatarSize = 36
                        )
                    }
                },
                trailingIcon = {
                    when {
                        userStatus == GuardianStatus.DECLINED -> {
                            Text(
                                text = stringResource(id = R.string.decline_text),
                                style = CCTheme.typography.common_text_medium,
                                modifier = Modifier.padding(end = 16.dp),
                                color = CCTheme.colors.primaryRed,
                            )
                        }
                        userStatus == GuardianStatus.NEW -> TextActionButton(
                            actionTitle = stringResource(
                                id = R.string.add_text
                            )
                        ) {
                            userStatus = GuardianStatus.PENDING
                            onRowAction.invoke(
                                NetworkMainActions.ClickAddUser(
                                    (PersonModel(
                                        personId = contact.contactId
                                    ))
                                )
                            )
                        }
                        userStatus == GuardianStatus.PENDING || contact.contactId in pendingList -> {
                            Text(
                                text = stringResource(id = R.string.pending_text),
                                style = CCTheme.typography.common_text_medium,
                                modifier = Modifier.padding(end = 16.dp),
                                color = CCTheme.colors.grayOne,
                            )
                        }
                        else -> {

                        }
                    }
                },
                rowClick = {
                    onRowAction.invoke(
                        NetworkMainActions.ClickUser(
                            GuardianItem(
                                guardianId = contact.contactId,
                                guardianName = contact.name,
                                guardianAvatar = null,
                                guardianStatus = GuardianStatus.NEW,
                                statusId = 0,
                            )
                        )
                    )
                },
            )
        }
        if ((results?.itemCount ?: 0) > 0) {
            item {
                HeaderTitleText(
                    stringResource(id = R.string.add_guardian_header_title),
                    needTop = results?.itemCount == 0
                )

            }
        }

        results?.let {
            itemsIndexed(results, key = { _, item -> item.personId }) { index, item ->
                Timber.tag("networkSearch").i("lazyList ${results.itemCount}")

                item?.let {
                    var userStatus by remember { mutableStateOf(item.outputRequest?.status) }
                    userStatus = item.outputRequest?.status ?: GuardianStatus.NEW
                    SearchRow(
                        title = item.personFullName,
                        searchPart = searchPart,
                        needDivider = index < results.itemCount - 1,
                        leadingIcon = {
                            item.personAvatar?.imageUrl?.let {
                                CircleUserAvatar(
                                    avatar = it,
                                    avatarSize = 36
                                )
                            }
                        },
                        trailingIcon = {
                            when {
                                it.isGuardian -> {}
                                userStatus == GuardianStatus.PENDING || item.personId in pendingList -> {
                                    Text(
                                        text = stringResource(id = R.string.pending_text),
                                        style = CCTheme.typography.common_text_medium,
                                        modifier = Modifier.padding(end = 16.dp),
                                        color = CCTheme.colors.grayOne,
                                    )
                                }
                                else -> {
                                    TextActionButton(actionTitle = stringResource(id = R.string.add_text)) {
                                        userStatus = GuardianStatus.PENDING
                                        onRowAction.invoke(NetworkMainActions.ClickAddUser(item))
                                    }
                                }
                            }
                        },
                        rowClick = {
                            onRowAction.invoke(NetworkMainActions.ClickUser(item.mapToItem()))
                        }
                    )
                }

            }
        }
        item {
            RowDivider()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun SearchRow(
    title: String,
    searchPart: String = "",
    needDivider: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    rowClick: () -> Unit,
) {

    val searchStart = title.lowercase().indexOf(searchPart.lowercase())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(CCTheme.colors.white)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = CCTheme.colors.black),
            ) { rowClick.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text =
                    buildAnnotatedString {
                        append(title)
                        addStyle(
                            style = SpanStyle(
                                color = CCTheme.colors.black,
                            ),
                            start = 0,
                            end = title.length
                        )
                        addStyle(
                            style = SpanStyle(
                                color = CCTheme.colors.primaryRed,
                            ),
                            start = searchStart,
                            end = searchStart + searchPart.length
                        )
                    },
                    style = CCTheme.typography.common_text_medium,
                    modifier = Modifier.weight(1f)
                )

                if (trailingIcon != null) trailingIcon()
            }
            if (needDivider) RowDividerGrayThree(0)
        }
    }
}



