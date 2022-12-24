package com.civilcam.settings_feature.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.domainLayer.model.subscription.SubscriptionBaseInfo
import com.civilcam.ext_features.DateUtils
import com.civilcam.ext_features.compose.elements.ProfileRow
import com.civilcam.ext_features.compose.elements.RowDivider
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.settings_feature.R
import com.civilcam.settings_feature.model.SettingsActions

@Composable
fun SubscriptionSettingsContent(
	subscriptionPlan: SubscriptionBaseInfo,
	onAction: (SettingsActions) -> Unit,
	onRestoreClicked: () -> Unit,
	onSubscriptionPlanClick: () -> Unit,
) {

	LaunchedEffect(key1 = true) {
		onAction.invoke(SettingsActions.ClickGoSubscription)
	}

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
			title = stringResource(id = R.string.settings_manage_subscription_plan),
			needDivider = true,
			rowClick = { onAction.invoke(SettingsActions.GoSubscriptionManage) }
		)


		ProfileRow(
			title = stringResource(id = R.string.settings_restore_purchase),
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
	subscriptionPlan: SubscriptionBaseInfo,
	rowClick: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(CCTheme.colors.white)
			//.clickable { rowClick.invoke() }
			.padding(horizontal = 16.dp, vertical = 12.dp),
	) {

		Text(
			text = subscriptionPlan.title,
			color = CCTheme.colors.black,
			style = CCTheme.typography.common_medium_text_regular
		)

		Spacer(modifier = Modifier.width(4.dp))

		Text(
			text = when (subscriptionPlan.productId) {
				IN_APP_TRIAL -> stringResource(id = R.string.subscription_trial_description)
				else -> stringResource(
					id = R.string.subscription_description,
					subscriptionPlan.cost / 100.0,
					subscriptionPlan.unitType
				)
			},
			color = CCTheme.colors.grayOne,
			style = CCTheme.typography.common_medium_text_regular,
			fontWeight = FontWeight.W400,
			fontSize = 15.sp
		)

		if (subscriptionPlan.expiredAt.isNotEmpty()) {
			Text(
				text = stringResource(
					id = R.string.subscription_renews_value, DateUtils.getDateFromIso(
						subscriptionPlan.expiredAt
					)
				),
				color = CCTheme.colors.grayOne,
				style = CCTheme.typography.common_medium_text_regular,
				fontWeight = FontWeight.W400,
				fontSize = 15.sp
			)
		}
	}
}

private const val IN_APP_TRIAL = "in_app_trial"
