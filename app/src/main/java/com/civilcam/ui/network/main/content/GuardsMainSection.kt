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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domainLayer.model.guard.GuardianItem
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.domainLayer.model.guard.LetterGuardItem
import com.civilcam.domainLayer.model.guard.NetworkType
import com.civilcam.ui.common.compose.*
import com.civilcam.ui.network.main.model.NetworkMainModel

@Composable
fun GuardsMainSection(
    screenData: NetworkMainModel,
    tabPage: NetworkType,
    clickGoRequests: () -> Unit,
    clickGoUser: (GuardianItem) -> Unit
) {

    if (screenData.guardiansList.isEmpty() && screenData.requestsList.isEmpty()) {
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
            if (screenData.requestsList.isNotEmpty()) {
                GuardRequestRowSection(
                    screenData.requestsList as List<GuardianItem>
                ) {
                    clickGoRequests.invoke()
                }
            }

            LazyColumn {
                itemsIndexed(screenData.guardiansList) { index, contact ->
                    when (contact) {
                        is LetterGuardItem -> Box {
                            Column {
                                HeaderTitleText(contact.letter)
                            }
                        }


                        is GuardianItem -> {
                            InformationRow(
                                needDivider = (if (index == screenData.guardiansList.lastIndex) {
                                    false
                                } else {
                                    screenData.guardiansList[index + 1] is GuardianItem
                                }),
                                title = contact.guardianName,
                                leadingIcon = {
                                    CircleUserAvatar(
                                        avatar = contact.guardianAvatar,
                                        avatarSize = 36
                                    )
                                },
                                trailingIcon = {
                                    getUserStatus(contact.guardianStatus)
                                }
                            ) {
                                clickGoUser.invoke(contact)
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
private fun getUserStatus(guardianStatus: GuardianStatus) {
    var text = ""
    var textColor = CCTheme.colors.black

    when (guardianStatus) {
        GuardianStatus.PENDING -> {
            text = stringResource(id = R.string.pending_text)
            textColor = CCTheme.colors.grayOne
        }
        GuardianStatus.DECLINED -> {
            text = stringResource(id = R.string.decline_text)
            textColor = CCTheme.colors.primaryRed
        }
        GuardianStatus.NEED_HELP -> {
            text = stringResource(id = R.string.network_main_help_me)
            textColor = CCTheme.colors.black
        }
        GuardianStatus.SAFE -> {
            text = stringResource(id = R.string.network_main_safe)
            textColor = CCTheme.colors.grayOne
        }
        else -> {}
    }

    Text(
        text = text,
        style = CCTheme.typography.common_text_medium,
        modifier = Modifier.padding(end = 16.dp),
        color = textColor,
    )
}