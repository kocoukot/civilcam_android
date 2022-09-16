package com.civilcam.ext_features

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

interface SearchQuery {

    val mTextSearch: MutableStateFlow<String>
    private val textSearch: StateFlow<String>
        get() = mTextSearch.asStateFlow()

    @OptIn(FlowPreview::class)
    fun query(coroutine: CoroutineScope, queryString: (String) -> Unit) {
        coroutine.launch {
            textSearch.debounce(400).collect { query ->
                queryString.invoke(query)
            }
        }
    }
}