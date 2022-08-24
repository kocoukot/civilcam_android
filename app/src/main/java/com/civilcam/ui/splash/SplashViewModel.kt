package com.civilcam.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.civilcam.domainLayer.model.CurrentUser
import com.civilcam.domainLayer.usecase.user.GetLocalCurrentUserUseCase
import com.civilcam.domainLayer.usecase.user.IsUserLoggedInUseCase

class SplashViewModel(
	getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase,
	isUserLoggedInUseCase: IsUserLoggedInUseCase,
) : ViewModel() {
	
	private val _user = MutableLiveData<CurrentUser?>()
	val user: LiveData<CurrentUser?> = _user
	
	init {
		_user.value = if (isUserLoggedInUseCase())
			getLocalCurrentUserUseCase()
		else
			null
	}
}