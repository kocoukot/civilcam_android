package com.civilcam.ui.subscription

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.SubscriptionPlan
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.subscription.model.SubscriptionActions

@Composable
fun SubscriptionScreenContent(viewModel: SubscriptionViewModel) {
	
	val state = viewModel.state.collectAsState()
	
	Surface(modifier = Modifier.fillMaxSize()) {
		
		Box(
			modifier = Modifier.fillMaxWidth(),
			contentAlignment = Alignment.TopCenter,
		) {
			
			Column(
				modifier = Modifier.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Top
			) {
				Image(
					painter = painterResource(R.drawable.img_subscription_background),
					contentDescription = null,
					modifier = Modifier.fillMaxWidth(),
					contentScale = ContentScale.FillWidth
				)
			}
			
			Row(
				Modifier
					.fillMaxWidth()
					.padding(top = 24.dp, start = 16.dp)
			) {
				IconButton(onClick = { viewModel.setInputActions(SubscriptionActions.GoBack) }) {
					
					Icon(
						painter = painterResource(id = R.drawable.ic_back_navigation),
						contentDescription = null,
						tint = CCTheme.colors.white
					)
					
				}
			}
			
		}
		
		Column(
			modifier = Modifier
				.navigationBarsPadding(),
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
						horizontal = 32.dp,
						vertical = 16.dp
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
						SubscriptionOption(
							stringResource(id = R.string.subscription_safety_network)
						)
						
						SubscriptionOption(
							stringResource(id = R.string.subscription_emergency_alert)
						)
						
						SubscriptionOption(
							stringResource(id = R.string.subscription_live_stream)
						)
						
						SubscriptionOption(
							stringResource(id = R.string.subscription_safety_map)
						)
						
						SubscriptionOption(
							stringResource(id = R.string.subscription_detailed_alert)
						)
					}
					
					Spacer(modifier = Modifier.height(18.dp))
					
					LazyColumn(
						modifier = Modifier.fillMaxWidth()
					) {
						items(state.value.subscriptionPlans) { plan ->
							SubscriptionPlanRow(
								subscriptionPlan = plan,
								onButtonClicked = { },
								isActivated = false
							)
						}
					}
					
					Spacer(modifier = Modifier.height(40.dp))
					
					ComposeButton(
						title = stringResource(id = R.string.start_text),
						modifier = Modifier
							.fillMaxWidth()
							.navigationBarsPadding(),
						isActivated = true,
						buttonClick = {
						
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
	Text(modifier = Modifier
		.padding(top = 4.dp),
		text = buildAnnotatedString {
			withStyle(
				style = SpanStyle(
					color = CCTheme.colors.primaryRed,
					fontSize = 15.sp,
					fontWeight = FontWeight.W400
				),
			) {
				append("• ")
			}
			withStyle(
				style = SpanStyle(
					color = CCTheme.colors.black,
					fontSize = 15.sp,
					fontWeight = FontWeight.W400
				),
			) {
				append(value)
			}
		})
}

@Composable
fun SubscriptionPlanRow(
	subscriptionPlan: SubscriptionPlan,
	onButtonClicked: () -> Unit,
	isActivated: Boolean = false
) {
	
	val backgroundColor = if (isActivated) CCTheme.colors.primaryRed else CCTheme.colors.white
	val borderColor = if (isActivated) CCTheme.colors.primaryRed else CCTheme.colors.grayOne
	val titleColor = if (isActivated) CCTheme.colors.white else CCTheme.colors.black
	val textColor = if (isActivated) CCTheme.colors.white else CCTheme.colors.grayOne
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 6.dp)
			.clip(RoundedCornerShape(8.dp))
			.border(
				1.dp,
				borderColor,
				RoundedCornerShape(8.dp)
			)
			.background(color = backgroundColor)
			.clickable { onButtonClicked.invoke() },
		horizontalAlignment = Alignment.Start
	) {
		
		Text(
			text = subscriptionPlan.subscriptionPlan,
			style = CCTheme.typography.common_text_medium,
			color = titleColor,
			modifier = Modifier.padding(start = 12.dp, top = 12.dp)
		)
		
		Text(
			text = subscriptionPlan.purchasePrice,
			style = CCTheme.typography.common_text_small_regular,
			color = textColor,
			modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
		)
	}
}
