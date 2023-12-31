package com.civilcam.ui.network.main.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.domainLayer.model.guard.GuardianItem
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.domainLayer.model.guard.LetterGuardItem
import com.civilcam.domainLayer.model.guard.NetworkType
import com.civilcam.ext_features.compose.elements.*
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.network.main.model.NetworkMainActions
import com.civilcam.ui.network.main.model.NetworkMainModel

@Composable
fun GuardsMainSection(
    screenData: NetworkMainModel,
    tabPage: NetworkType,
    onAction: (NetworkMainActions) -> Unit,
) {

    if ((tabPage == NetworkType.ON_GUARD && screenData.onGuardList.isEmpty() && screenData.requestsList.isEmpty()) ||
        (tabPage == NetworkType.GUARDIANS && screenData.guardiansList.isEmpty())
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            EmptyListText(
                stringResource(
                    id =
                    when (tabPage) {
                        NetworkType.ON_GUARD -> R.string.network_main_on_guard_list_is_empty
                        NetworkType.GUARDIANS -> R.string.network_main_guardians_list_is_empty
                    }
                )
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            if (screenData.requestsList.isNotEmpty() && tabPage == NetworkType.ON_GUARD) {
                GuardRequestRowSection(
                    screenData.requestsList as List<GuardianItem>
                ) { onAction.invoke(NetworkMainActions.ClickGoRequests) }
            }

            LazyColumn {
                val screenList = when (tabPage) {
                    NetworkType.ON_GUARD -> screenData.onGuardList
                    NetworkType.GUARDIANS -> screenData.guardiansList
                }
                itemsIndexed(screenList) { index, contact ->
                    when (contact) {
                        is LetterGuardItem -> Box {
                            Column {
                                HeaderTitleText(contact.letter)
                            }
                        }


                        is GuardianItem -> {
                            InformationRow(
                                needDivider = (if (index == screenList.lastIndex) {
                                    false
                                } else {
                                    screenList[index + 1] is GuardianItem
                                }),
                                title = contact.guardianName,
                                leadingIcon = {
                                    contact.guardianAvatar?.imageUrl?.let {
                                        CircleUserAvatar(
                                            avatar = it,
                                            avatarSize = 36
                                        )
                                    }
                                },
                                trailingIcon = {
                                    UserStatusText(contact.guardianStatus)
                                }
                            ) {
                                onAction.invoke(NetworkMainActions.ClickUser(contact))
                            }
                        }
                    }
                }

                item {
                    RowDivider()
                }
            }
        }
    }
}

@Composable
private fun UserStatusText(guardianStatus: GuardianStatus) {
    val textColor: Color
    val text = when (guardianStatus) {
        GuardianStatus.DECLINED -> {
            textColor = CCTheme.colors.primaryRed
            stringResource(id = R.string.decline_text)
        }
        GuardianStatus.NEED_HELP -> {
            textColor = CCTheme.colors.black
            stringResource(id = R.string.network_main_help_me)
        }
        GuardianStatus.SAFE -> {
            textColor = CCTheme.colors.grayOne
            stringResource(id = R.string.network_main_safe)
        }
        GuardianStatus.PENDING -> {
            textColor = CCTheme.colors.grayOne
            stringResource(id = R.string.pending_text)
        }
        else -> {
            textColor = CCTheme.colors.grayOne
            ""
        }
    }

    Text(
        text = text,
        style = CCTheme.typography.common_text_medium,
        modifier = Modifier.padding(end = 16.dp),
        color = textColor,
    )
}