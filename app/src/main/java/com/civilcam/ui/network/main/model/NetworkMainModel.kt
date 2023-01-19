package com.civilcam.ui.network.main.model

import com.civilcam.data.local.model.Contact
import com.civilcam.domainLayer.model.guard.GuardItem


data class NetworkMainModel(
    val requestsList: List<GuardItem> = emptyList(),
    val onGuardList: List<GuardItem> = emptyList(),
    val guardiansList: List<GuardItem> = emptyList(),
    var searchText: String = "",
    val searchScreenSectionModel: SearchScreenSectionModel = SearchScreenSectionModel(),
    val contactsList: List<Contact> = emptyList(),
)

data class SearchScreenSectionModel(
    val pendingList: List<Int> = emptyList()
)