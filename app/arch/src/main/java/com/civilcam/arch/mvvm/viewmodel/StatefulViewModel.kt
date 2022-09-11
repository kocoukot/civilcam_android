//package com.civilcam.arch.mvvm.viewmodel
//
//import androidx.lifecycle.ViewModel
//import com.civilcam.arch.mvvm.state.ScreenState
//import io.reactivex.Completable
//import io.reactivex.Maybe
//import io.reactivex.Observable
//import io.reactivex.Single
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.disposables.CompositeDisposable
//import io.reactivex.disposables.Disposable
//import io.reactivex.rxkotlin.addTo
//import java.util.concurrent.ConcurrentLinkedQueue
//
//abstract class StatefulViewModel<State : ScreenState> : ViewModel() {
//    abstract val state: State
//
//    protected open val sideEffectHandler: SideEffectHandler = SideEffectHandler()
//
//    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
//    private var onAttachPending: ConcurrentLinkedQueue<OnAttachAction> = ConcurrentLinkedQueue()
//    private var onDetachPending: MutableList<OnDetachAction> = mutableListOf()
//    private var isAttachedFirstTime: Boolean = false
//    private var isAttached: Boolean = false
//
//    override fun onCleared() {
//        compositeDisposable.clear()
//    }
//
//    open fun onAttached() {}
//
//    open fun onDetached() {}
//
//    fun attach() {
//        isAttached = true
//        onAttachPending.forEach {
//            it.action.invoke()
//            if (it.executeAtFirstAttach && !isAttachedFirstTime) {
//                onAttachPending.remove()
//            }
//        }
//        isAttachedFirstTime = true
//        onAttached()
//    }
//
//    fun detach() {
//        onDetachPending.forEach {
//            it.action.invoke()
//        }
//        isAttached = false
//        onDetached()
//    }
//
//    protected fun doOnAttach(executeAtFirstAttach: Boolean = false, action: () -> Unit) {
//        if (isAttached) {
//            action.invoke()
//        } else {
//            onAttachPending.add(OnAttachAction(action, executeAtFirstAttach))
//        }
//    }
//
//    protected fun doOnDetach(action: () -> Unit) {
//        if (!isAttached) {
//            action.invoke()
//        } else {
//            onDetachPending.add(OnDetachAction(action))
//        }
//    }
//
//    protected fun <T> Single<T>.execute(onSuccess: (T) -> Unit) =
//        this.observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { sideEffectHandler.onLoading?.invoke(true) }
//            .doAfterSuccess { sideEffectHandler.onLoading?.invoke(false) }
//            .subscribe(onSuccess) { sideEffectHandler.onError?.invoke(it) }
//            .disposeOnCleared()
//
//    protected fun <T> Observable<T>.execute(
//        onComplete: () -> Unit = {},
//        onSuccess: (T) -> Unit
//    ) = this.observeOn(AndroidSchedulers.mainThread())
//        .subscribe(onSuccess, { sideEffectHandler.onError?.invoke(it) }, onComplete)
//        .disposeOnCleared()
//
//    protected fun <T> Maybe<T>.execute(
//        onComplete: () -> Unit = {},
//        onSuccess: (T) -> Unit
//    ) = this.observeOn(AndroidSchedulers.mainThread())
//        .doOnSubscribe { sideEffectHandler.onLoading?.invoke(true) }
//        .doOnSuccess { sideEffectHandler.onLoading?.invoke(false) }
//        .subscribe(onSuccess, { sideEffectHandler.onError?.invoke(it) }, onComplete)
//        .disposeOnCleared()
//
//    protected fun Completable.execute(
//        onComplete: () -> Unit = {}
//    ) = this.observeOn(AndroidSchedulers.mainThread())
//        .doOnSubscribe { sideEffectHandler.onLoading?.invoke(true) }
//        .doOnComplete { sideEffectHandler.onLoading?.invoke(false) }
//        .subscribe(onComplete, { sideEffectHandler.onError?.invoke(it) })
//        .disposeOnCleared()
//
//    protected fun Disposable.disposeOnCleared() = this.addTo(compositeDisposable)
//}