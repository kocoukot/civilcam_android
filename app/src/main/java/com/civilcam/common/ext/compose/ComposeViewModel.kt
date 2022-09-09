package com.civilcam.common.ext.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civilcam.arch.common.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class ComposeViewModel<A : ComposeFragmentState, R : ComposeFragmentRoute, T : ComposeFragmentActions> :
    ViewModel() {

    protected abstract var _state: MutableStateFlow<A>
    val state by lazy { _state.asStateFlow() }

    private val _steps: SingleLiveEvent<R> = SingleLiveEvent()
    val steps: SingleLiveEvent<R> = _steps


    protected fun navigateRoute(route: R) {
        _steps.value = route
    }

    abstract fun setInputActions(action: T)

    protected abstract fun clearErrorText()

    protected fun <Response> networkRequest(
        action: suspend () -> Response,
        onSuccess: (Response) -> Unit,
        onFailure: (Throwable) -> Unit,
        onComplete: (() -> Unit)? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { action.invoke() }
                .onSuccess { onSuccess.invoke(it) }
                .onFailure { error ->
                    onFailure.invoke(error)
                }.also { onComplete?.invoke() }
        }
    }
}
