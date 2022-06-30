package com.civilcam.ui.network.main.model

import com.civilcam.domain.model.guard.GuardianModel


data class NetworkMainModel(
    val requestsList: List<GuardItem> = emptyList(),
    val guardiansList: List<GuardItem> = emptyList(),
    var searchText: String = "",
    var searchResult: List<GuardianModel> = emptyList()

//    val searchScreenSectionModel: SearchScreenSectionModel = SearchScreenSectionModel()
)
//
//data class SearchScreenSectionModel(
//)