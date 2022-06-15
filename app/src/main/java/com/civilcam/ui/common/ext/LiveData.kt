package com.civilcam.ui.common.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

inline fun <T> LiveData<T>.observeNonNull(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    this.observe(owner, {
        if (it != null) {
            observer(it)
        }
    })
}

fun <T> observableLiveData(rxChain: () -> Observable<T>, onError: (Throwable) -> Unit) =
    RxLiveData<T> {
        rxChain()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(it::onChanged, onError)
    }

fun <T> singleLiveData(rxChain: () -> Single<T>, onError: (Throwable) -> Unit) =
    RxLiveData<T> {
        rxChain()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(it::onChanged, onError)
    }

fun <T> maybeLiveData(rxChain: () -> Maybe<T>, onError: (Throwable) -> Unit) =
    RxLiveData<T> {
        rxChain()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(it::onChanged, onError)
    }

class RxLiveData<T>(private val rxChain: (observer: Observer<in T>) -> Disposable) :
    LiveData<T>() {
    private var disposable: Disposable = Disposables.disposed()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
        if (!hasActiveObservers()) {
            disposable = rxChain(observer)
        }
    }

    override fun onInactive() {
        disposable.dispose()
        super.onInactive()
    }
}

fun <T> Fragment.observe(liveData: LiveData<T>, observer: Observer<T>) =
    liveData.observe(viewLifecycleOwner, observer)