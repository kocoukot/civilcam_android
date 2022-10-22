package com.civilcam.alert_feature.history.content

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
import com.civilcam.alert_feature.R
import com.civilcam.alert_feature.history.model.AlertHistoryActions
import com.civilcam.domainLayer.model.alerts.AlertDetailModel
import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.ext_features.DateUtils.alertDateFormat
import com.civilcam.ext_features.compose.elements.CircleUserAvatar
import com.civilcam.ext_features.compose.elements.InformationBoxContent
import com.civilcam.ext_features.compose.elements.InformationRow
import com.civilcam.ext_features.compose.elements.RowDivider
import com.civilcam.ext_features.ext.phoneNumberFormat
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun AlertHistoryDetailScreenContent(
    alertDetail: AlertDetailModel?,
    onScreenAction: (AlertHistoryActions) -> Unit,
    alertType: AlertType
) {
    alertDetail?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CCTheme.colors.white),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            alertDetail.alertModel.userInfo.personAvatar?.imageUrl?.let { avatar ->
                CircleUserAvatar(
                    avatar = avatar,
                    avatarSize = 120,
                )
            }

            Text(
                text = alertDetail.alertModel.userInfo.personFullName,
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
                    alertDetail.alertModel.userInfo.personPhone?.let {
                        InformationBoxContent(
                            text = it.phoneNumberFormat(),
                            modifier = Modifier.weight(1f),
                            onButtonClick = {
                                onScreenAction.invoke(AlertHistoryActions.CLickCallUser)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
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
                title = stringResource(id = R.string.alert_user_detail_initiated_title),
                titleFont = FontFamily(Font(com.civilcam.ext_features.R.font.roboto_regular)),
                needDivider = true,
                isClickable = false,
                trailingIcon = {
                    DetailInformationText(alertDateFormat(alertDetail.alertModel.alertDate))
                }) {}

            InformationRow(
                title = stringResource(id = R.string.alert_user_detail_location_title),
                titleFont = FontFamily(Font(com.civilcam.ext_features.R.font.roboto_regular)),
                needDivider = true,
                isClickable = false,
                trailingIcon = {
                    DetailInformationText(alertDetail.alertModel.alertLocation)
                }) {}

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.alert_user_detail_resolved_by_title),
                    style = CCTheme.typography.common_text_regular,
                    color = CCTheme.colors.black,
                    modifier = Modifier.padding(start = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .padding(end = 16.dp)

                ) {
                    items(if (alertType == AlertType.RECEIVED) alertDetail.alertResolvers.take(1) else alertDetail.alertResolvers) { item ->
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = CCTheme.colors.primaryRed,
                                        fontWeight = FontWeight.W400,
                                        fontSize = 13.sp
                                    ),
                                ) {
                                    append("${item.userInfo.personFullName} ")
                                }

                                withStyle(
                                    style = SpanStyle(
                                        color = CCTheme.colors.grayOne,
                                        fontWeight = FontWeight.W400,
                                        fontSize = 13.sp
                                    ),
                                ) {
                                    append(alertDateFormat(item.resolveDate))
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