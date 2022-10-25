package com.civilcam.ext_features.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civilcam.ext_features.compose.ComposeFragmentActions
import com.civilcam.ext_features.compose.ComposeFragmentRoute
import com.civilcam.ext_features.compose.ComposeFragmentState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface BaseViewModel : RouteCommunication {


    abstract class Base<State : ComposeFragmentState>(
        private val mSteps: Channel<ComposeFragmentRoute> = Channel(),
        private val mState: MutableStateFlow<State>,
    ) : ViewModel(), BaseViewModel, StateCommunication<State> {

        override val state = mState.asStateFlow()
        override fun observeSteps(): Flow<ComposeFragmentRoute> = mSteps.receiveAsFlow()

        override fun sendRoute(event: ComposeFragmentRoute) {
            viewModelScope.launch { mSteps.send(event) }
        }

        override fun updateInfo(info: suspend State.() -> State) {
            viewModelScope.launch {
                mState.update { info.invoke(it) }
            }
        }

        abstract fun setInputActions(action: ComposeFragmentActions)

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

interface RouteCommunication {
    fun observeSteps(): Flow<ComposeFragmentRoute>

    fun sendRoute(event: ComposeFragmentRoute)
}