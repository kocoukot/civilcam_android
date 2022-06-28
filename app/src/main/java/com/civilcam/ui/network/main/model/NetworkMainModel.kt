package com.civilcam.ui.network.main.model


data class NetworkMainModel(
    val requestsList: List<GuardItem> = emptyList(),
    val guardiansList: List<GuardItem> = emptyList()
)