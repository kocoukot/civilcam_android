package com.civilcam.ui.alerts.history.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.ui.alerts.history.model.AlertHistoryActions
import com.civilcam.ui.alerts.history.model.ResolvedUsers
import com.civilcam.ui.common.compose.CircleUserAvatar
import com.civilcam.ui.common.compose.InformationRow
import com.civilcam.ui.common.compose.RowDivider
import com.civilcam.ui.profile.userDetails.content.InformationBoxContent

@Composable
fun AlertHistoryDetailScreenContent(
    onScreenAction: (AlertHistoryActions) -> Unit,
    alertType: AlertType
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CCTheme.colors.white),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CircleUserAvatar(
            avatar = R.drawable.img_avatar,
            avatarSize = 120,
        )

        Text(
            text = "Sylvanas Windrunner",
            style = CCTheme.typography.button_text,
            color = CCTheme.colors.black,
            modifier = Modifier.padding(vertical = 16.dp)
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (alertType == AlertType.RECEIVED) {
                InformationBoxContent(
                    text = "+1 (123) 456 7890",
                    modifier = Modifier.weight(1f),
                    onButtonClick = {
                        onScreenAction.invoke(AlertHistoryActions.CLickCallUser)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            val buttonModifier = if (alertType == AlertType.RECEIVED) {
                Modifier.weight(1f)
            } else {
                Modifier
            }
            InformationBoxContent(
                text = stringResource(id = R.string.history_detail_download_video),
                modifier = buttonModifier,
                textModifier = Modifier.padding(horizontal = if (alertType == AlertType.RECEIVED) 0.dp else 34.dp),
                onButtonClick = {
                    onScreenAction.invoke(AlertHistoryActions.CLickUploadVideo)
                }
            )
        }
        RowDivider()
        Divider(
            color = CCTheme.colors.lightGray, modifier = Modifier
                .height(32.dp)
        )
        RowDivider()

        InformationRow(
            title = "Alert initiated",
            titleFont = FontFamily(Font(R.font.roboto_regular)),
            needDivider = true,
            isClickable = false,
            trailingIcon = {
                DetailInformationText("02.02.2022, 1:41 PM")
            }) {}

        InformationRow(
            title = "Location",
            titleFont = FontFamily(Font(R.font.roboto_regular)),
            needDivider = true,
            isClickable = false,
            trailingIcon = {
                DetailInformationText("1456 Broadway, New York, NY, 10023")
            }) {}

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Resolved by",
                style = CCTheme.typography.common_text_regular,
                color = CCTheme.colors.black,
                modifier = Modifier.padding(start = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .padding(end = 16.dp)

            ) {
                items(if (alertType == AlertType.RECEIVED) resolvedDataList.take(1) else resolvedDataList) { item ->

                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = CCTheme.colors.primaryRed,
                                    fontWeight = FontWeight.W400,
                                    fontSize = 13.sp
                                ),
                            ) {
                                append("${item.resolverName} ")
                            }

                            withStyle(
                                style = SpanStyle(
                                    color = CCTheme.colors.grayOne,
                                    fontWeight = FontWeight.W400,
                                    fontSize = 13.sp
                                ),
                            ) {
                                append(item.resolverDate)
                            }
                        },
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }

        RowDivider()
    }
}

@Composable
private fun DetailInformationText(
    text: String
) {
    Text(
        text = text,
        style = CCTheme.typography.common_text_small_regular,
        color = CCTheme.colors.grayOne,
        modifier = Modifier.padding(end = 16.dp)
    )
}

private val resolvedDataList = listOf(
    ResolvedUsers(
        "Arthas Menethil",
        "02.02.2022, 2:43 PM"
    ),
    ResolvedUsers(
        "Arthas Menethil",
        "02.02.2022, 2:43 PM"
    ),
    ResolvedUsers(
        "Arthas Menethil",
        "02.02.2022, 2:43 PM"
    ),
    ResolvedUsers(
        "Arthas Menethil",
        "02.02.2022, 2:43 PM"
    ),
)