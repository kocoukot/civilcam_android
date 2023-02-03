package com.civilcam.ui.subscription

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.domainLayer.model.subscription.SubscriptionsList
import com.civilcam.domainLayer.model.subscription.UserSubscriptionState
import com.civilcam.ext_features.alert.AlertDialogType
import com.civilcam.ext_features.compose.elements.AlertDialogComp
import com.civilcam.ext_features.compose.elements.ComposeButton
import com.civilcam.ext_features.compose.elements.DialogLoadingContent
import com.civilcam.ext_features.compose.elements.IconActionButton
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.subscription.SubscriptionFragment.Companion.IN_APP_TRIAL
import com.civilcam.ui.subscription.model.SubscriptionActions

@Composable
fun SubscriptionScreenContent(viewModel: SubscriptionViewModel) {
	
	val state = viewModel.state.collectAsState()
	
	var selectedSubscription by remember { mutableStateOf(state.value.selectedSubscriptionType) }
	
	if (state.value.isLoading) {
		DialogLoadingContent()
	}
	
	state.value.alert
		.takeIf { it != AlertDialogType.Empty }
		?.let { alert ->
			AlertDialogComp(
				dialogTitle = alert.title(),
				dialogText = alert.text(),
				alertType = alert.alertButtons(),
			) {
				viewModel.setInputActions(SubscriptionActions.CloseAlert(it))
			}
		}
	
	Surface(modifier = Modifier.fillMaxSize()) {
		
		Box(
			modifier = Modifier.fillMaxWidth(),
			contentAlignment = Alignment.TopCenter,
		) {
			
			Image(
				painter = painterResource(R.drawable.img_subscription_background),
				contentDescription = null,
				modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
				contentScale = ContentScale.FillWidth
			)
			
			IconActionButton(
				modifier = Modifier
                    .padding(top = 24.dp, start = 16.dp)
                    .align(Alignment.TopStart),
				buttonIcon = R.drawable.ic_back_navigation, tint = CCTheme.colors.white
			) {
				viewModel.setInputActions(SubscriptionActions.GoBack)
			}
			
		}
		
		Column(
			modifier = Modifier.navigationBarsPadding(),
			verticalArrangement = Arrangement.Bottom,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			
			Card(
				shape = RoundedCornerShape(
					topStart = 12.dp, topEnd = 12.dp
				),
				backgroundColor = CCTheme.colors.white,
				modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
				elevation = 8.dp,
			) {
				Column(
					modifier = Modifier.padding(
						horizontal = 32.dp, vertical = 16.dp
					),
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					
					Spacer(modifier = Modifier.height(4.dp))
					
					Text(
						text = stringResource(R.string.subscription_options_title),
						style = CCTheme.typography.big_title,
						color = CCTheme.colors.primaryRed,
						textAlign = TextAlign.Center,
					)
					
					Spacer(modifier = Modifier.height(12.dp))
					
					Text(
						text = stringResource(R.string.subscription_no_commitment_title),
						style = CCTheme.typography.common_medium_text_regular,
						color = CCTheme.colors.black,
						textAlign = TextAlign.Center,
					)
					
					Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
						horizontalAlignment = Alignment.Start
					) {
						for (textString in listOf(
                            stringResource(id = R.string.subscription_safety_network),
                            stringResource(id = R.string.notification_alert_title),
                            stringResource(id = R.string.subscription_live_stream),
                            stringResource(id = R.string.subscription_safety_map),
                            stringResource(id = R.string.subscription_detailed_alert),
                        )) {
							SubscriptionOption(textString)
						}
					}
					
					Spacer(modifier = Modifier.height(18.dp))
					
					LazyColumn(
						modifier = Modifier.fillMaxWidth()
					) {
						
						items(state.value.subscriptionsList.list) { subscription ->
							SubscriptionPlanRow(
								subscriptionInfo = subscription,
								isActivated = selectedSubscription.productId == subscription.productId,
								onButtonClicked = {
									selectedSubscription = it
								},
							)
						}
					}
					
					
					Spacer(modifier = Modifier.height(40.dp))
					
					ComposeButton(
						title = stringResource(
							when (state.value.userSubState) {
								UserSubscriptionState.FIRST_LAUNCH -> R.string.start_text
								UserSubscriptionState.SUB_RESELECT -> R.string.subscriptions_change_plan
								UserSubscriptionState.SUB_EXPIRED -> R.string.subscriptions_resubscribe_plan
							}
						),
						modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding(),
						buttonClick = {
							viewModel.setInputActions(
								SubscriptionActions.OnSubSelect(
									selectedSubscription
								)
							)
						},
					)
				}
			}
		}
	}
}

@Composable
fun SubscriptionOption(
	value: String
) {
	Text(modifier = Modifier.padding(top = 4.dp), text = buildAnnotatedString {
		withStyle(
			style = SpanStyle(
				color = CCTheme.colors.primaryRed, fontSize = 15.sp, fontWeight = FontWeight.W400
			),
		) {
			append("• ")
		}
		withStyle(
			style = SpanStyle(
				color = CCTheme.colors.black, fontSize = 15.sp, fontWeight = FontWeight.W400
			),
		) {
			append(value)
		}
	})
}

@Composable
fun SubscriptionPlanRow(
	subscriptionInfo: SubscriptionsList.SubscriptionInfo,
	onButtonClicked: (SubscriptionsList.SubscriptionInfo) -> Unit,
	isActivated: Boolean = false
) {
	
	val backgroundColor by animateColorAsState(
		targetValue = if (isActivated) CCTheme.colors.primaryRed else CCTheme.colors.white
	)
	val borderColor = if (isActivated) CCTheme.colors.primaryRed else CCTheme.colors.grayOne
	val titleColor = if (isActivated) CCTheme.colors.white else CCTheme.colors.black
	val textColor = if (isActivated) CCTheme.colors.white else CCTheme.colors.grayOne
	
	Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)
        .clip(RoundedCornerShape(8.dp))
        .border(
            1.dp, borderColor, RoundedCornerShape(8.dp)
        )
        .drawBehind {
            drawRect(color = backgroundColor)
        }
        .clickable { onButtonClicked.invoke(subscriptionInfo) },
		horizontalAlignment = Alignment.Start
	) {
		
		Text(
			text = subscriptionInfo.title,
			style = CCTheme.typography.common_text_medium,
			color = titleColor,
			modifier = Modifier.padding(start = 12.dp, top = 12.dp)
		)
		
		Text(
			text = when (subscriptionInfo.productId) {
                IN_APP_TRIAL -> stringResource(id = com.civilcam.settings_feature.R.string.subscription_trial_description)
                else -> stringResource(
                    id = com.civilcam.settings_feature.R.string.subscription_description,
                    subscriptionInfo.cost / 100,
                    subscriptionInfo.unitType
                )
			},
			style = CCTheme.typography.common_text_small_regular,
			color = textColor,
			modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
		)
	}
}
