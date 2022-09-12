package com.civilcam.ui.profile.userProfile.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.ext_features.compose.elements.RowDivider
import com.civilcam.ext_features.compose.elements.RowDividerGrayThree
import com.civilcam.ext_features.ext.formatPhoneNumber
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.profile.userProfile.model.UserProfileActions
import com.civilcam.ui.profile.userProfile.model.UserProfileType

@Composable
fun MainProfileContent(
    data: CurrentUser,
    onRowClicked: (UserProfileActions) -> Unit,
) {
	Column(
		modifier = Modifier.fillMaxWidth()
	) {
        RowDivider()
		for (type in UserProfileType.values()) {
			val rowValue = when (type) {
				UserProfileType.PHONE_NUMBER -> data.userBaseInfo.phone.formatPhoneNumber()
				UserProfileType.EMAIL -> data.sessionUser.email
				UserProfileType.PIN_CODE -> "••••"
			}
			ProfileRow(
				title = stringResource(id = type.title),
				value = rowValue,
				needDivider = type != UserProfileType.PIN_CODE,
				rowClick = { onRowClicked.invoke(UserProfileActions.GoCredentials(type)) }
			)
		}
        RowDivider()
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
		if (needDivider) RowDividerGrayThree()
	}
}
