package com.civilcam.ui.subscription

import androidx.lifecycle.viewModelScope
import com.civilcam.domainLayer.model.subscription.UserSubscriptionState
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.subscriptions.GetSubscriptionsUseCase
import com.civilcam.domainLayer.usecase.subscriptions.GetUserSubscriptionUseCase
import com.civilcam.domainLayer.usecase.subscriptions.SetUserSubscriptionUseCase
import com.civilcam.domainLayer.usecase.user.LogoutUseCase
import com.civilcam.ext_features.DateUtils
import com.civilcam.ext_features.alert.AlertDialogType
import com.civilcam.ext_features.compose.ComposeViewModel
import com.civilcam.ui.subscription.data.SubConfirmAlert
import com.civilcam.ui.subscription.data.SubConfirmed
import com.civilcam.ui.subscription.data.SubExpired
import com.civilcam.ui.subscription.model.SubscriptionActions
import com.civilcam.ui.subscription.model.SubscriptionRoute
import com.civilcam.ui.subscription.model.SubscriptionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class SubscriptionViewModel(
	private val userSubState: UserSubscriptionState,
	private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
	private val setUserSubscriptionUseCase: SetUserSubscriptionUseCase,
	private val getSubscriptionUseCase: GetUserSubscriptionUseCase,
	private val logoutUseCase: LogoutUseCase
) : ComposeViewModel<SubscriptionState, SubscriptionRoute, SubscriptionActions>() {
	override var _state: MutableStateFlow<SubscriptionState> = MutableStateFlow(SubscriptionState())

	init {
		_state.update { it.copy(userSubState = userSubState) }
		if (userSubState == UserSubscriptionState.SUB_EXPIRED) {
			_state.update { it.copy(alert = SubExpired) }

		}
		getCurrentSubscription()
	}

	override fun setInputActions(action: SubscriptionActions) {
		when (action) {
			SubscriptionActions.GoBack -> goBack()
			is SubscriptionActions.OnSubSelect -> onSubSelected(action.title)
			is SubscriptionActions.CloseAlert -> onCloseAlert(action.alertDecision)
		}
	}

	private fun onCloseAlert(isConfirm: Boolean) {
		when (state.value.alert) {
			is SubConfirmAlert -> {
				if (isConfirm) onConfirmSubscription()
			}
			is SubConfirmed -> {
				goProfileSetup()
			}
		}
		_state.update { it.copy(alert = AlertDialogType.Empty) }
	}

	private fun getSubscriptions() {
		_state.update { it.copy(isLoading = true) }
		viewModelScope.launch {
			kotlin.runCatching { getSubscriptionsUseCase.getSubscriptions() }.onSuccess { data ->
				_state.update {
					it.copy(
						subscriptionsList = data, isLoading = false
					)
				}
			}.onFailure { error ->
				error.serviceCast { errorText, _, _ ->
					_state.update { it.copy(alert = AlertDialogType.ErrorAlert(errorText)) }
				}
			}
			_state.update { it.copy(isLoading = false) }
		}
	}

	private fun getCurrentSubscription() {
		_state.value = _state.value.copy(isLoading = true)
		viewModelScope.launch {
			kotlin.runCatching { getSubscriptionUseCase.getUserSubscription() }.onSuccess { data ->
				_state.update {
					it.copy(
						subscription = data, selectedSubscriptionType = data.title
					)
				}
				getSubscriptions()
			}.onFailure { error ->
				error.serviceCast { errorText, _, _ ->
					_state.update { it.copy(alert = AlertDialogType.ErrorAlert(errorText)) }
				}
			}
			_state.value = _state.value.copy(isLoading = false)
		}
	}

	private fun goProfileSetup() {
		navigateRoute(
			when (userSubState) {
				UserSubscriptionState.FIRST_LAUNCH -> SubscriptionRoute.GoProfileSetup
				UserSubscriptionState.SUB_RESELECT -> SubscriptionRoute.GoBack
				UserSubscriptionState.SUB_EXPIRED -> SubscriptionRoute.GoMap
			}
		)
		_state.update { it.copy(purchaseSuccess = false) }
	}

	private fun goBack() {
		if (userSubState == UserSubscriptionState.SUB_EXPIRED) {
			networkRequest(
				action = { logoutUseCase() },
				onSuccess = { navigateRoute(SubscriptionRoute.GoCreateAccount) },
				onFailure = {},
				onComplete = {},
			)
		} else {
			navigateRoute(SubscriptionRoute.GoBack)
		}
	}

	private fun onConfirmSubscription() {
		//todo api request for subscription

		viewModelScope.launch {
			_state.update { it.copy(isLoading = true) }
			delay(2000)
			_state.update { it.copy(alert = SubConfirmed, isLoading = false) }
		}
	}

	private fun onSubSelected(subscriptionType: String) {
		if (subscriptionType == TRIAL) {
			_state.value = _state.value.copy(alert = SubConfirmed)
		} else {
			_state.value.subscriptionsList.list.find { it.title == subscriptionType }
				?.let { selectedSub ->
					var date = LocalDate.now()
					date = when (selectedSub.unitType) {
						"month" -> date.plusMonths(1)
						"year" -> date.plusYears(1)
						else -> date
					}
					_state.update {
						it.copy(
							alert = SubConfirmAlert(
								"$${selectedSub.cost / 100}",
								date.format(DateUtils.dateOfBirthFormatter)
							)
						)
					}

				}
		}
	}

	override fun clearErrorText() {
		_state.update { it.copy(alert = AlertDialogType.Empty) }
	}

	companion object {
		private const val TRIAL = "Trial"
	}
}