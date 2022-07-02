package com.civilcam.ui.network.main

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.model.guard.GuardianModel
import com.civilcam.domain.model.guard.GuardianStatus
import com.civilcam.domain.model.guard.NetworkType
import com.civilcam.domain.usecase.guardians.GetGuardsListUseCase
import com.civilcam.domain.usecase.guardians.GetGuardsRequestsUseCase
import com.civilcam.domain.usecase.guardians.SearchGuardsResultUseCase
import com.civilcam.ui.network.main.model.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.*


@OptIn(FlowPreview::class)
class NetworkMainViewModel(
    private val getGuardsListUseCase: GetGuardsListUseCase,
    private val getGuardsRequestsUseCase: GetGuardsRequestsUseCase,
    private val searchGuardsResultUseCase: SearchGuardsResultUseCase

) :
    ComposeViewModel<NetworkMainState, NetworkMainRoute, NetworkMainActions>() {
    override var _state: MutableStateFlow<NetworkMainState> = MutableStateFlow(NetworkMainState())

    private val _textSearch = MutableStateFlow("")
    private val textSearch: StateFlow<String> = _textSearch.asStateFlow()

    init {
        navBarStatus()
        viewModelScope.launch {
            _state.value = _state.value.copy(data = NetworkMainModel())

            textSearch.debounce(400).collect { query ->
                query
                    .takeIf { it.isNotEmpty() }
                    ?.let {
//                        Timber.d("guardianSearchQuery $it")
                        try {
                            val result = searchGuardsResultUseCase.searchGuards(it)
//                            Timber.d("guardianSearchQuery result $result")
                            setSearchResult(result)
                        } catch (e: Exception) {
//                            Timber.d("guardianSearchQuery error ${e.localizedMessage}")
                            _state.value =
                                _state.value.copy(errorText = e.localizedMessage)
                        }
                    } ?: run {
                    setSearchResult(emptyList())
                }
            }

        }
    }

    override fun setInputActions(action: NetworkMainActions) {
        when (action) {
            NetworkMainActions.ClickGoMyProfile -> changeLoadState()
            NetworkMainActions.ClickGoSettings -> goSettings()
            is NetworkMainActions.ClickNetworkTypeChange -> changeAlertType(action.networkType)
            NetworkMainActions.ClickGoRequests -> goRequests()
            is NetworkMainActions.ClickUser -> goUser(action.user)
            NetworkMainActions.ClickGoBack -> goBack()
            NetworkMainActions.ClickAddGuardian -> addGuardian()
            NetworkMainActions.ClickGoSearch -> searchGuard()
            NetworkMainActions.ClickGoContacts -> goContacts()
            is NetworkMainActions.EnteredSearchString -> searchContact(action.searchQuery)
            is NetworkMainActions.ClickAddUser -> addUser(action.user)
        }
    }

    private fun goBack() {
        when (_state.value.screenState) {
            NetworkScreen.MAIN -> {}
            NetworkScreen.REQUESTS -> {
                _state.value =
                    _state.value.copy(screenState = NetworkScreen.MAIN)
            }
            NetworkScreen.SEARCH_GUARD, NetworkScreen.ADD_GUARD -> {
                _state.value = _state.value.copy(
                    screenState = NetworkScreen.MAIN,
                )
            }

        }
        navBarStatus()
    }

    private fun addGuardian() {
        _state.value = _state.value.copy(screenState = NetworkScreen.ADD_GUARD)
        navBarStatus()
    }

    private fun goContacts() {
        _steps.value = NetworkMainRoute.GoContacts
    }

    private fun goUser(user: GuardianItem) {
        _steps.value = NetworkMainRoute.GoUserDetail(user.guardianId)
    }

    private fun searchGuard() {
        if (_state.value.screenState == NetworkScreen.MAIN) {
            _state.value = _state.value.copy(screenState = NetworkScreen.SEARCH_GUARD)
            navBarStatus()
        }
    }

    private fun goSettings() {
        _steps.value = NetworkMainRoute.GoSettings
    }

    private fun goRequests() {
        _state.value = _state.value.copy(screenState = NetworkScreen.REQUESTS)
        navBarStatus()
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

    private fun searchContact(searchString: String) {
        var searchData = _state.value.data
        searchData?.let {
            searchData = searchData!!.copy(searchText = searchString)
            _state.value = _state.value.copy(data = searchData!!.copy())
            _textSearch.value = searchString

        }
    }

    private fun addUser(user: GuardianModel) {
        viewModelScope.launch {
            val contactsModel =
                _state.value.data?.searchScreenSectionModel ?: SearchScreenSectionModel()
            contactsModel.searchResult.let { contacts ->
                contacts.find { it.guardianId == user.guardianId }?.guardianStatus =
                    GuardianStatus.PENDING

            }
            _state.value =
                _state.value.copy(
                    data = _state.value.data!!.copy(searchScreenSectionModel = contactsModel).copy()
                )

//            _state.value.data?.let { model ->
//
//                model.searchResult.find { it.guardianId == user.guardianId }?.guardianStatus =
//                    GuardianStatus.PENDING
//                _state.value = _state.value.copy(data = model.copy())
////                Timber.d("addUser ${_state.value.data}")
//            }
        }
    }

    private fun setSearchResult(result: List<GuardianModel>) {
        var data = _state.value.data
        data?.let {
            data = data!!.copy(searchScreenSectionModel = SearchScreenSectionModel(result))
            _state.value = _state.value.copy(data = data!!.copy())
        }
    }

    fun navBarStatus() {
        _steps.value =
            NetworkMainRoute.IsNavBarVisible(_state.value.screenState != NetworkScreen.MAIN)

    }
}





    



