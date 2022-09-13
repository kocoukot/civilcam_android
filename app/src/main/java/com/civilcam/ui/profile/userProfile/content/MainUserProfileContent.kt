package com.civilcam.ui.profile.userProfile.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.ext_features.compose.elements.ProfileRow
import com.civilcam.ext_features.compose.elements.RowDivider
import com.civilcam.ext_features.ext.formatPhoneNumber
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

