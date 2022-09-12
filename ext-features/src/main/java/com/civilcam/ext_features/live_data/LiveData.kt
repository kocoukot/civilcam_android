package com.civilcam.ext_features.live_data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

inline fun <T> LiveData<T>.observeNonNull(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    this.observe(owner) {
        if (it != null) {
            observer(it)
        }
    }
}

//fun <T> observableLiveData(rxChain: () -> Observable<T>, onError: (Throwable) -> Unit) =
//    RxLiveData<T> {
//        rxChain()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(it::onChanged, onError)
//    }
//
//fun <T> singleLiveData(rxChain: () -> Single<T>, onError: (Throwable) -> Unit) =
//    RxLiveData<T> {
//        rxChain()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(it::onChanged, onError)
//    }
//
//fun <T> maybeLiveData(rxChain: () -> Maybe<T>, onError: (Throwable) -> Unit) =
//    RxLiveData<T> {
//        rxChain()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(it::onChanged, onError)
//    }
//
//class RxLiveData<T>(private val rxChain: (observer: Observer<in T>) -> Disposable) :
//    LiveData<T>() {
//    private var disposable: Disposable = Disposables.disposed()
//
//    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
//        super.observe(owner, observer)
//        if (!hasActiveObservers()) {
//            disposable = rxChain(observer)
//        }
//    }
//
//    override fun onInactive() {
//        disposable.dispose()
//        super.onInactive()
//    }
//}
//
//fun <T> Fragment.observe(liveData: LiveData<T>, observer: Observer<T>) =
//    liveData.observe(viewLifecycleOwner, observer)