package com.civilcam.domainLayer.model

data class SearchModel(
    val searchResult: List<AutocompletePlace> = emptyList(),
    val searchQuery: String = ""
)
