package com.civilcam.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.civilcam.domainLayer.model.CurrentUser
import com.civilcam.domainLayer.repos.AccountRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class SplashViewModel(
	private val accountRepository: AccountRepository
) : ViewModel() {
	
	private val compositeDisposable: CompositeDisposable = CompositeDisposable()
	
	private val _user = MutableLiveData<CurrentUser?>()
	val user: LiveData<CurrentUser?> = _user
	
	init {
		if (accountRepository.isUserLoggedIn) {
			accountRepository.getUser()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeBy(
					onSuccess = {
						_user.value = it
					},
					onError = {
						Timber.e(it)
						_user.value = null
					}
				)
				.addTo(compositeDisposable)
		} else {
			_user.value = null
		}
	}
	
	
}