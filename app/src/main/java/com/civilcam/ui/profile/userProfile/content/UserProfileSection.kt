package com.civilcam.ui.profile.userProfile.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.TextActionButton
import com.civilcam.ui.profile.userDetails.model.UserDetailsModel
import com.civilcam.ui.profile.userProfile.model.UserProfileScreen
import com.civilcam.utils.DateUtils

@Composable
fun UserProfileSection(
	userData: UserDetailsModel,
	screenType: UserProfileScreen,
	onBackButtonClick: () -> Unit,
	onActionClick: (UserProfileScreen) -> Unit,
	mockAction: () -> Unit
) {
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 12.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		
		Row(Modifier.fillMaxWidth()) {
			Spacer(modifier = Modifier.width(8.dp))
			BackButton { onBackButtonClick.invoke() }
			Spacer(modifier = Modifier.weight(1f))
			Image(
				painter = painterResource(id = R.drawable.img_avatar),
				contentDescription = null,
				modifier = Modifier
					.size(120.dp)
					.clip(RoundedCornerShape(50))
					.padding(top = 16.dp)
					.clickable {
						mockAction.invoke()
					},
			)
			Spacer(modifier = Modifier.weight(1f))
			TextActionButton(
				isEnabled = true,
				actionTitle = when (screenType) {
					UserProfileScreen.PROFILE -> stringResource(id = R.string.user_profile_edit_title)
					UserProfileScreen.EDIT -> stringResource(id = R.string.save_text)
				}
			) {
				onActionClick.invoke(screenType)
			}
		}
		
		AnimatedVisibility(visible = screenType == UserProfileScreen.PROFILE) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = userData.userInfoSection.userName,
					style = CCTheme.typography.button_text,
					color = CCTheme.colors.black,
					modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
				)
				
				AdditionalInfo(DateUtils.dateOfBirthFormat(userData.userInfoSection.dateOfBirth))
				
				Spacer(modifier = Modifier.height(4.dp))
				
				AdditionalInfo(userData.userInfoSection.address)
				
				Spacer(modifier = Modifier.height(12.dp))
			}
		}
		
		AnimatedVisibility(visible = screenType == UserProfileScreen.EDIT) {
			Text(
				text = stringResource(id = R.string.user_profile_change_profile_image_title),
				style = CCTheme.typography.button_text,
				color = CCTheme.colors.primaryRed,
				modifier = Modifier.padding(top = 16.dp),
				fontWeight = FontWeight.W500
			)
		}
		
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

@Composable
private fun InformationBoxContent(
	text: String,
	modifier: Modifier = Modifier,
	onButtonClick: (() -> Unit)? = null
) {
	Box(
		modifier = modifier
			.background(CCTheme.colors.white, RoundedCornerShape(50))
			.border(1.dp, CCTheme.colors.grayOne, RoundedCornerShape(50))
			.clip(RoundedCornerShape(50))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = rememberRipple(color = CCTheme.colors.black),
				enabled = !text.contains("+")
			) {
				if (!text.contains("+")) onButtonClick?.invoke()
			},
		contentAlignment = Alignment.Center
	) {
		Text(
			text = text,
			style = CCTheme.typography.common_text_small_medium,
			modifier = Modifier.padding(vertical = 8.dp),
			textAlign = TextAlign.Center
		)
	}
}