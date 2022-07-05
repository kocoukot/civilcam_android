package com.civilcam.ui.profile.userDetails.content

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
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.profile.userDetails.model.UserDetailsType

@Composable
fun MainProfileContent(
	onRowClicked: (UserDetailsType) -> Unit
) {
	Column(
		modifier = Modifier.fillMaxWidth()
	) {
		
		Spacer(modifier = Modifier.height(30.dp))
		Divider(color = CCTheme.colors.grayThree)
		for (type in UserDetailsType.values()) {
			ProfileRow(
				title = stringResource(id = type.title),
				value = "",
				needDivider = false,
				rowClick = { onRowClicked.invoke(type) }
			)
		}
		Divider(color = CCTheme.colors.grayThree)
	}
}

@Composable
fun ProfileRow(
	title: String,
	value: String,
	titleColor: Color = CCTheme.colors.black,
	needDivider: Boolean = true,
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
				color = CCTheme.colors.grayOne,
				style = CCTheme.typography.common_medium_text_regular
			)
			Icon(
				painter = painterResource(id = R.drawable.ic_row_arrow),
				contentDescription = null
			)
		}
		if (needDivider) Divider(
			color = CCTheme.colors.grayThree,
			modifier = Modifier.padding(start = 16.dp)
		)
	}
}
