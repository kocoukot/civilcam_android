package com.civilcam.ui.profile.userProfile.content

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.ext_features.DateUtils
import com.civilcam.ext_features.compose.elements.InputField
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.profile.setup.content.CalendarIcon
import com.civilcam.ui.profile.setup.model.UserInfoDataType
import com.civilcam.ui.profile.userProfile.model.UserProfileActions

@Composable
fun UserProfileEditContent(
    userData: CurrentUser,
    onValueChanged: (UserInfoDataType, String) -> Unit,
    onActionClicked: (UserProfileActions) -> Unit,
) {
	var dateOfBirth by remember { mutableStateOf("") }

	Column(
		modifier = Modifier
            .fillMaxWidth()
            .background(color = CCTheme.colors.lightGray)
            .padding(horizontal = 16.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		
		InputField(
			title = stringResource(id = R.string.profile_setup_first_name_label),
			text = userData.userBaseInfo.firstName.orEmpty(),
			placeHolder = stringResource(id = R.string.profile_setup_first_name_placeholder),
			isReversed = true
		) {
			onValueChanged.invoke(UserInfoDataType.FIRST_NAME, it)
		}
		
		Spacer(
			modifier = Modifier
                .height(16.dp)
                .background(color = CCTheme.colors.lightGray)
		)
		
		InputField(
			title = stringResource(id = R.string.profile_setup_last_name_label),
			text = userData.userBaseInfo.lastName.orEmpty(),
			placeHolder = stringResource(id = R.string.profile_setup_last_name_placeholder),
			isReversed = true
		) {
			onValueChanged.invoke(UserInfoDataType.LAST_NAME, it)
		}
		
		Spacer(
			modifier = Modifier
                .height(16.dp)
                .background(color = CCTheme.colors.lightGray)
		)

		dateOfBirth =
			userData.userBaseInfo.dob.takeIf { it.isNotEmpty() }
				?.let { DateUtils.dateOfBirthFormat(it) }.orEmpty()
		
		val calendarColor =
			animateColorAsState(targetValue = if (dateOfBirth.isEmpty()) CCTheme.colors.grayOne else CCTheme.colors.primaryRed)
		InputField(
            isEnable = false,
            text = dateOfBirth,
            trailingIcon = { CalendarIcon(calendarColor.value) },
            title = stringResource(id = R.string.profile_setup_date_of_birth),
            placeHolder = stringResource(id = R.string.profile_setup_date_of_birth_placeholder),
            onTextClicked = {
                onActionClicked.invoke(UserProfileActions.ClickDateSelect)
            },
            onValueChanged = {},
            isReversed = true
        )
		
		Spacer(
			modifier = Modifier
                .height(16.dp)
                .background(color = CCTheme.colors.lightGray)
		)
		
		InputField(
			isEnable = false,
			title = stringResource(id = R.string.profile_setup_address_label),
			text = userData.userBaseInfo.address,
			placeHolder = stringResource(id = R.string.profile_setup_address_placeholder),
			isReversed = true,
			onValueChanged = {},
			onTextClicked = {
				onActionClicked.invoke(UserProfileActions.ClickLocationSelect)
			}
		)
		
	}
}