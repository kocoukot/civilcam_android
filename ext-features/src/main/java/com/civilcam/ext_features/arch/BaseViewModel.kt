package com.civilcam.ext_features.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface BaseViewModel<Route> : SendEvent<Route>, ObserveSteps<Route> {


    open class Base<State, Action, Route>(
        private val mSteps: Channel<Route> = Channel(),
        private val mState: MutableStateFlow<State>,
    ) : ViewModel(), BaseViewModel<Route>, StateCommunication<State>, ReceiveEvent<Action> {

        override val state = mState.asStateFlow()
        override fun observeSteps(): Flow<Route> = mSteps.receiveAsFlow()

        override fun sendEvent(event: Route) {
            viewModelScope.launch { mSteps.send(event) }
        }

        override fun updateInfo(info: suspend State.() -> State) {
            viewModelScope.launch {
                mState.update { info.invoke(it) }
            }
        }

        override fun setInputActions(action: Action) {}

        protected open fun clearErrorText() {}

        protected fun getState() = mState.value

        protected fun <Response> networkRequest(
            action: suspend () -> Response,
            onSuccess: (Response) -> Unit,
            onFailure: (Throwable) -> Unit,
            onComplete: (() -> Unit)? = null
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                kotlin.runCatching { action.invoke() }
                    .onSuccess {
                        withContext(Dispatchers.Main) {
                            onSuccess.invoke(it)
                        }
                    }
                    .onFailure { error ->
                        onFailure.invoke(error)
                    }.also { onComplete?.invoke() }
            }
        }
    }

}

interface ObserveSteps<T> {
    fun observeSteps(): Flow<T>
}

interface SendEvent<R> {
    fun sendEvent(event: R)
}

interface ReceiveEvent<A> {
    fun setInputActions(action: A)
}
