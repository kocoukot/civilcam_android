package com.civilcam.ui.network.main.content

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
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domainLayer.model.guard.GuardianModel
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.ui.common.compose.*
import timber.log.Timber

@Composable
fun GuardianSearchContent(
    data: List<GuardianModel>,
    searchPart: String,
    clickAddNew: (GuardianModel) -> Unit

) {
    Timber.d("addUser ${data}")

    if (data.isEmpty()) {
        EmptySearchScreenState()
    } else {
        SearchResults(data, searchPart,
            clickAddNew = {
                clickAddNew.invoke(it)
            })
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
    results: List<GuardianModel>,
    searchPart: String,
    clickAddNew: (GuardianModel) -> Unit
) {
    LazyColumn {
        item {
            HeaderTitleText(
                stringResource(id = R.string.add_guardian_header_title),
                needTop = results.isEmpty()
            )

        }

        itemsIndexed(results, key = { _, item -> item.guardianId }) { index, item ->
            var userStatus by remember { mutableStateOf(item.guardianStatus) }
            userStatus = item.guardianStatus
            SearchRow(
                title = item.guardianName,
                searchPart = searchPart,
                needDivider = index < results.lastIndex,
                leadingIcon = {
                    CircleUserAvatar(
                        avatar = item.guardianAvatar,
                        avatarSize = 36
                    )
                },
                trailingIcon = {
                    when (userStatus) {
                        GuardianStatus.PENDING -> {
                            Text(
                                text = stringResource(id = R.string.pending_text),
                                style = CCTheme.typography.common_text_medium,
                                modifier = Modifier.padding(end = 16.dp),
                                color = CCTheme.colors.grayOne,
                            )
                        }
                        GuardianStatus.NEW -> {
                            TextActionButton(actionTitle = stringResource(id = R.string.add_text)) {
                                userStatus = GuardianStatus.PENDING
                                clickAddNew.invoke(item)
                            }
                        }
                        else -> {}
                    }

                },
            ) {

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
    searchPart: String,
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



