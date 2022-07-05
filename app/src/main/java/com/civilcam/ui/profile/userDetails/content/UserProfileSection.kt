package com.civilcam.ui.profile.userDetails.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.profile.userDetails.model.UserDetailsModel
import com.civilcam.ui.profile.userDetails.model.UserDetailsScreen
import com.civilcam.utils.DateUtils

@Composable
fun UserProfileSection(
	userData: UserDetailsModel,
	userProfileType: UserDetailsScreen,
	mockAction: () -> Unit
) {
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			
			.padding(top = 12.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		
		Image(
			painter = painterResource(id = R.drawable.img_avatar),
			contentDescription = null,
			modifier = Modifier
				.size(120.dp)
				.clip(RoundedCornerShape(50))
				.clickable {
					mockAction.invoke()
				}
		)
		
		Crossfade(targetState = userProfileType) {
			AnimatedVisibility(visible = it == UserDetailsScreen.PROFILE) {
				Text(
					text = userData.userInfoSection.userName,
					style = CCTheme.typography.button_text,
					color = CCTheme.colors.black,
					modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
				)
			}
			
			AnimatedVisibility(visible = it == UserDetailsScreen.EDIT) {
				Text(
					text = stringResource(id = R.string.user_profile_change_image),
					style = CCTheme.typography.button_text,
					color = CCTheme.colors.primaryRed,
					modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
					textAlign = TextAlign.Center
				)
			}
		}
		
		AdditionalInfo(DateUtils.dateOfBirthFormat(userData.userInfoSection.dateOfBirth))
		
		AdditionalInfo(userData.userInfoSection.address)
		
		Spacer(modifier = Modifier.height(12.dp))
		
		Divider(color = CCTheme.colors.grayThree)
	}
}

@Composable
private fun AdditionalInfo(text: String, modifier: Modifier = Modifier) {
	Text(
		modifier = modifier,
		text = text,
		style = CCTheme.typography.common_text_medium,
		color = CCTheme.colors.grayOne,
	)
}