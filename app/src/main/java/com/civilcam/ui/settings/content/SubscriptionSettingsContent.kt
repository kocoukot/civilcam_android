package com.civilcam.ui.settings.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.SubscriptionPlan
import com.civilcam.ui.common.compose.RowDivider
import com.civilcam.ui.profile.userProfile.content.ProfileRow

@Composable
fun SubscriptionSettingsContent(
	subscriptionPlan: SubscriptionPlan,
	onManageClicked: () -> Unit,
	onRestoreClicked: () -> Unit,
	onSubscriptionPlanClick: () -> Unit
) {
	Column(
		Modifier.fillMaxWidth()
	) {

		Spacer(modifier = Modifier.height(32.dp))

		RowDivider()

		SubscriptionStatus(
			subscriptionPlan = subscriptionPlan,
			rowClick = { onSubscriptionPlanClick.invoke() }
		)
		RowDivider()

		Spacer(modifier = Modifier.height(30.dp))

		RowDivider()

		ProfileRow(
			title = stringResource(id = R.string.manage_subscription_plan),
			needDivider = true,
			rowClick = onManageClicked
		)

		ProfileRow(
			title = stringResource(id = R.string.restore_purchase),
			titleColor = CCTheme.colors.primaryRed,
			needDivider = false,
			needRow = false,
			rowClick = onRestoreClicked
		)
		RowDivider()
	}
}


@Composable
fun SubscriptionStatus(
	subscriptionPlan: SubscriptionPlan,
	rowClick: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(CCTheme.colors.white)
			.clickable { rowClick.invoke() }
			.padding(horizontal = 16.dp, vertical = 12.dp),
	) {
		
		Text(
			text = subscriptionPlan.subscriptionPeriod,
			color = CCTheme.colors.black,
			style = CCTheme.typography.common_medium_text_regular
		)
		
		Spacer(modifier = Modifier.width(4.dp))
		
		Text(
			text = subscriptionPlan.subscriptionPlan,
			color = CCTheme.colors.grayOne,
			style = CCTheme.typography.common_medium_text_regular,
			fontWeight = FontWeight.W400,
			fontSize = 15.sp
		)
		
		Text(
			text = "Renews: ${subscriptionPlan.autoRenewDate}",
			color = CCTheme.colors.grayOne,
			style = CCTheme.typography.common_medium_text_regular,
			fontWeight = FontWeight.W400,
			fontSize = 15.sp
		)
	}
}
