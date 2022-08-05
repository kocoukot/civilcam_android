package com.civilcam.common.ext.compose

import androidx.lifecycle.ViewModel
import com.civilcam.arch.common.livedata.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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
}