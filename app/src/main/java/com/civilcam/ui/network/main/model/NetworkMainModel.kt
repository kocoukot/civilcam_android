package com.civilcam.ui.network.main.model

import com.civilcam.domainLayer.model.guard.GuardItem


data class NetworkMainModel(
    val requestsList: List<GuardItem> = emptyList(),
    val guardiansList: List<GuardItem> = emptyList(),
    var searchText: String = "",
//    var searchResult: List<GuardianModel> = emptyList()

    val searchScreenSectionModel: SearchScreenSectionModel = SearchScreenSectionModel()
)

//
data class SearchScreenSectionModel(
    var searchResult: List<com.civilcam.domainLayer.model.guard.GuardianModel> = emptyList()
)