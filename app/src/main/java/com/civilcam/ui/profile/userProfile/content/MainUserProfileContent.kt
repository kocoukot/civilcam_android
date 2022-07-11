package com.civilcam.ui.profile.userProfile.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.UserInfo
import com.civilcam.ui.profile.userProfile.model.UserProfileType

@Composable
fun MainProfileContent(
	data: UserInfo,
	onRowClicked: (UserProfileType) -> Unit,
) {
	Column(
		modifier = Modifier.fillMaxWidth()
	) {
		Divider(color = CCTheme.colors.grayThree)
		for (type in UserProfileType.values()) {
			when(type) {
				UserProfileType.PHONE_NUMBER -> {
					ProfileRow(
						title = stringResource(id = type.title),
						value = data.phoneNumber,
						needDivider = type != UserProfileType.PIN_CODE,
						rowClick = { onRowClicked.invoke(type) }
					)
				}
				UserProfileType.EMAIL -> {
					ProfileRow(
						title = stringResource(id = type.title),
						value = data.email,
						needDivider = type != UserProfileType.PIN_CODE,
						rowClick = { onRowClicked.invoke(type) }
					)
				}
				UserProfileType.PIN_CODE -> {
					ProfileRow(
						title = stringResource(id = type.title),
						value = data.pinCode,
						needDivider = type != UserProfileType.PIN_CODE,
						rowClick = { onRowClicked.invoke(type) }
					)
				}
			}
		}
		Divider(color = CCTheme.colors.grayThree)
	}
}

@Composable
fun ProfileRow(
	title: String,
	value: String = "",
	titleColor: Color = CCTheme.colors.black,
	valueColor: Color = CCTheme.colors.grayOne,
	needDivider: Boolean = true,
	needRow: Boolean = true,
	rowClick: () -> Unit,
) {
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.height(45.dp)
			.background(CCTheme.colors.white)
			.clickable { rowClick.invoke() },
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(start = 16.dp)
				.weight(1f),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Start
		) {
			Text(
				text = title,
				color = titleColor,
				style = CCTheme.typography.common_medium_text_regular
			)
			Spacer(modifier = Modifier.weight(1f))
			Text(
				text = value,
				color = valueColor,
				style = CCTheme.typography.common_medium_text_regular,
				fontWeight = FontWeight.W400
			)
			Spacer(modifier = Modifier.width(4.dp))
			if (needRow) {
				Icon(
					painter = painterResource(id = R.drawable.ic_row_arrow),
					contentDescription = null,
					tint = CCTheme.colors.grayOne
				)
			}
			Spacer(modifier = Modifier.width(16.dp))
		}
		if (needDivider) Divider(
			color = CCTheme.colors.grayThree,
			modifier = Modifier.padding(start = 16.dp)
		)
	}
}
