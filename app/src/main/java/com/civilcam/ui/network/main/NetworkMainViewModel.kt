package com.civilcam.ui.network.main

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.model.guard.NetworkType
import com.civilcam.domain.usecase.guardians.GetGuardsListUseCase
import com.civilcam.domain.usecase.guardians.GetGuardsRequestsUseCase
import com.civilcam.ui.network.main.model.*
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*


class NetworkMainViewModel(
    private val getGuardsListUseCase: GetGuardsListUseCase,
    private val getGuardsRequestsUseCase: GetGuardsRequestsUseCase

) :
    ComposeViewModel<NetworkMainState, NetworkMainRoute, NetworkMainActions>() {
    override var _state: MutableStateFlow<NetworkMainState> = MutableStateFlow(NetworkMainState())

    init {
        viewModelScope.launch {
            _state.value =
                _state.value.copy(data = NetworkMainModel())
        }
    }

    override fun setInputActions(action: NetworkMainActions) {
        when (action) {
            NetworkMainActions.ClickGoMyProfile -> changeLoadState()
            NetworkMainActions.ClickGoSettings -> goSettings()
            NetworkMainActions.ClickAddGuardian -> TODO()
            is NetworkMainActions.ClickNetworkTypeChange -> changeAlertType(action.networkType)
            NetworkMainActions.ClickGoRequests -> goRequests()
            is NetworkMainActions.ClickUser -> TODO()
            NetworkMainActions.ClickGoBack -> goBack()
        }
    }

    private fun goBack() {
        _state.value = _state.value.copy(screenState = NetworkScreen.MAIN)
    }

    private fun goSettings() {
        _steps.value = NetworkMainRoute.GoSettings
    }

    private fun goRequests() {
        _state.value = _state.value.copy(screenState = NetworkScreen.REQUESTS)
    }


    private fun changeLoadState() {
        if (!_state.value.needToLoadMock) {
            _state.value = _state.value.copy(needToLoadMock = true)
            getMockInfo()
        }
    }

    private fun changeAlertType(networkType: NetworkType) {
        _state.value = _state.value.copy(networkType = networkType)
        if (_state.value.needToLoadMock) {
            getMockInfo()
        }
    }

    private fun getMockInfo() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val guardsList = async { getGuardsListUseCase.getGuards(_state.value.networkType) }
                val requestsList =
                    async { if (_state.value.networkType == NetworkType.ON_GUARD) getGuardsRequestsUseCase.getGuardRequests() else emptyList() }


                _state.value =
                    _state.value.copy(
                        data = NetworkMainModel(
                            requestsList = requestsList.await(),
                            guardiansList = mapToItems(guardsList.await()),
                        )
                    )

            } catch (e: Exception) {
                _state.value = _state.value.copy(errorText = e.localizedMessage)

            }.also {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private fun mapToItems(contacts: List<GuardianItem>): MutableList<GuardItem> {
        val items = mutableListOf<GuardItem>()
        val groupedContacts = TreeMap<Char, MutableList<GuardianItem>> { key1, key2 ->
            key1.compareTo(key2)
        }
        contacts.forEach {
            val charKey = if (it.guardianName.first().isLetter()) it.guardianName.first() else '#'
            groupedContacts.getOrPut(charKey) { mutableListOf() }
                .add(
                    GuardianItem(
                        guardianId = it.guardianId,
                        guardianName = it.guardianName,
                        guardianAvatar = it.guardianAvatar,
                        guardianStatus = it.guardianStatus,
                    )
                )
        }
        groupedContacts.forEach { (key, value) ->
            items.apply {
                add(LetterGuardItem(key.toString()))
                addAll(value)
            }
        }
        return items
    }

}


    



