package com.civilcam.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.civilcam.domainLayer.model.CurrentUser
import com.civilcam.domainLayer.repos.AccountRepository

class SplashViewModel(
	private val accountRepository: AccountRepository
) : ViewModel() {
	
	private val _user = MutableLiveData<CurrentUser?>()
	val user: LiveData<CurrentUser?> = _user
	
	init {
		if (accountRepository.isUserLoggedIn) {
			val user = accountRepository.getUser()
			_user.value = user
		} else {
			_user.value = null
		}
	}
	
}