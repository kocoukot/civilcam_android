package com.civilcam.ui.subscription

import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.civilcam.domainLayer.model.subscription.SubscriptionsList
import com.civilcam.domainLayer.model.subscription.UserSubscriptionState
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.subscriptions.GetSubscriptionsUseCase
import com.civilcam.domainLayer.usecase.subscriptions.GetUserSubscriptionUseCase
import com.civilcam.domainLayer.usecase.subscriptions.SetTrialSubscriptionUseCase
import com.civilcam.domainLayer.usecase.subscriptions.SetUserSubscriptionUseCase
import com.civilcam.domainLayer.usecase.user.GetLocalCurrentUserUseCase
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class SubscriptionViewModel(
	private val userSubState: UserSubscriptionState,
	private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
	private val setUserSubscriptionUseCase: SetUserSubscriptionUseCase,
	private val setTrialSubscriptionUseCase: SetTrialSubscriptionUseCase,
	private val getSubscriptionUseCase: GetUserSubscriptionUseCase,
	private val logoutUseCase: LogoutUseCase,
	private val getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase,
) : ComposeViewModel<SubscriptionState, SubscriptionRoute, SubscriptionActions>() {
	override var _state: MutableStateFlow<SubscriptionState> = MutableStateFlow(SubscriptionState())
	
	private lateinit var selectedSubType: SubscriptionsList.SubscriptionInfo
	
	init {
		_state.update { it.copy(userSubState = userSubState) }
		if (userSubState == UserSubscriptionState.SUB_EXPIRED) {
			_state.update { it.copy(alert = SubExpired) }
			
		}
		if (getLocalCurrentUserUseCase()?.subscription != null) {
			getCurrentSubscription()
		}
		getSubscriptions()
	}
	
	override fun setInputActions(action: SubscriptionActions) {
		when (action) {
			SubscriptionActions.GoBack -> goBack()
			is SubscriptionActions.OnSubSelect -> onSubSelected(action.subscription)
			is SubscriptionActions.CloseAlert -> onCloseAlert(action.alertDecision)
			is SubscriptionActions.SetProductList -> setProductList(action.list)
			is SubscriptionActions.SetPurchaseToken -> setPurchaseToken(action.token)
		}
	}
	
	private fun setTrialSubscription() {
		viewModelScope.launch {
			_state.update { it.copy(isLoading = true) }
			networkRequest(
				action = {
					setTrialSubscriptionUseCase(
						productId = selectedSubType.productId
					)
				},
				onSuccess = {
					_state.update { it.copy(alert = SubConfirmed, isLoading = false) }
				},
				onFailure = { error ->
					error.serviceCast { errorText, _, _ ->
						_state.update { it.copy(alert = AlertDialogType.ErrorAlert(errorText)) }
					}
				},
				onComplete = {
					_state.update { it.copy(isLoading = false) }
				},
			)
		}
	}
	
	private fun setPurchaseToken(token: String) {
		viewModelScope.launch {
			_state.update { it.copy(isLoading = true) }
			networkRequest(
				action = {
					setUserSubscriptionUseCase(
						receipt = token,
						productId = selectedSubType.productId
					)
				},
				onSuccess = {
					_state.update { it.copy(alert = SubConfirmed, isLoading = false) }
				},
				onFailure = { error ->
					error.serviceCast { errorText, _, _ ->
						_state.update { it.copy(alert = AlertDialogType.ErrorAlert(errorText)) }
					}
				},
				onComplete = {
					_state.update { it.copy(isLoading = false) }
				},
			)
		}
	}
	
	private fun setProductList(list: List<ProductDetails>) {
		_state.update { it.copy(productList = list) }
	}
	
	private fun onCloseAlert(isConfirm: Boolean) {
		when (state.value.alert) {
			is SubConfirmAlert -> if (isConfirm) onConfirmSubscription()
			is SubConfirmed -> goProfileSetup()
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
			kotlin.runCatching { getSubscriptionUseCase() }.onSuccess { data ->
				_state.update {
					it.copy(
						subscription = data,
						selectedSubscriptionType = SubscriptionsList.SubscriptionInfo(
							data.productId ?: "", data.title
						)
					)
				}
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
				UserSubscriptionState.SUB_EXPIRED -> {
					if (getLocalCurrentUserUseCase()?.sessionUser?.isPinCodeSet == true) SubscriptionRoute.GoMap else SubscriptionRoute.GoPinCode
				}
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
		steps.value =
			_state.value.selectedProductDetails?.let { SubscriptionRoute.LaunchPurchase(it) }
	}
	
	private fun onSubSelected(subscriptionType: SubscriptionsList.SubscriptionInfo) {
		_state.value.subscriptionsList.list.find { it == subscriptionType }
			?.let { selectedSub ->
				selectedSubType = selectedSub
				if (subscriptionType.title == TRIAL) {
					setTrialSubscription()
				} else {
					_state.update {
						it.copy(
							alert = SubConfirmAlert(
								"$${selectedSub.cost / 100}",
								LocalDate.now().format(DateUtils.dateOfBirthFormatter)
							)
						)
					}
					_state.value.productList.find { it.productId == subscriptionType.productId }
						.let { productDetails ->
							_state.update { it.copy(selectedProductDetails = productDetails) }
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