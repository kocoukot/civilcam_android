package com.civilcam.ui.network.main

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domainLayer.model.guard.*
import com.civilcam.domainLayer.usecase.guardians.GetGuardsListUseCase
import com.civilcam.domainLayer.usecase.guardians.GetGuardsRequestsUseCase
import com.civilcam.domainLayer.usecase.guardians.SearchGuardsResultUseCase
import com.civilcam.ui.common.ext.SearchQuery
import com.civilcam.ui.network.main.model.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*


class NetworkMainViewModel(
    private val screen: NetworkScreen = NetworkScreen.MAIN,
    private val getGuardsListUseCase: GetGuardsListUseCase,
    private val getGuardsRequestsUseCase: GetGuardsRequestsUseCase,
    private val searchGuardsResultUseCase: SearchGuardsResultUseCase

) : ComposeViewModel<NetworkMainState, NetworkMainRoute, NetworkMainActions>(), SearchQuery {
    override var _state: MutableStateFlow<NetworkMainState> = MutableStateFlow(NetworkMainState())
    override val mTextSearch = MutableStateFlow("")

    init {
        _state.update { it.copy(screenState = screen) }
        Timber.i("Screen type $screen")
        checkNavBarStatus()
        getMockInfo()
        _state.update { it.copy(data = NetworkMainModel()) }
        query(viewModelScope) { query ->
            query.takeIf { it.isNotEmpty() }
                ?.let {
                    viewModelScope.launch {
                        try {
                            val result = searchGuardsResultUseCase.searchGuards(it)
                            setSearchResult(result)
                        } catch (e: Exception) {
                            _state.update { it.copy(errorText = e.localizedMessage) }
                        }
                    }
                } ?: run {
                setSearchResult(emptyList())
            }
        }

    }

    override fun setInputActions(action: NetworkMainActions) {
        when (action) {
            NetworkMainActions.ClickGoMyProfile -> goMyProfile()
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
                _state.update { it.copy(screenState = NetworkScreen.MAIN) }
            }
            NetworkScreen.SEARCH_GUARD, NetworkScreen.ADD_GUARD -> {
                _state.update { it.copy(screenState = NetworkScreen.MAIN) }
            }
        }
        checkNavBarStatus()
    }

    private fun addGuardian() {
        _state.update { it.copy(screenState = NetworkScreen.ADD_GUARD) }
        checkNavBarStatus()
    }

    private fun goContacts() {
        navigateRoute(NetworkMainRoute.GoContacts)
    }

    private fun goMyProfile() {
        navigateRoute(NetworkMainRoute.GoProfile)
    }

    private fun goUser(user: GuardianItem) {
        navigateRoute(NetworkMainRoute.GoUserDetail(user.guardianId))
    }

    private fun searchGuard() {
        if (_state.value.screenState == NetworkScreen.MAIN) {
            _state.update { it.copy(screenState = NetworkScreen.SEARCH_GUARD) }
            checkNavBarStatus()
        }
    }

    private fun goSettings() {
        navigateRoute(NetworkMainRoute.GoSettings)
    }

    private fun goRequests() {
        _state.update { it.copy(screenState = NetworkScreen.REQUESTS) }
        checkNavBarStatus()
    }

    private fun changeAlertType(networkType: NetworkType) {
        _state.update { it.copy(networkType = networkType) }
        getMockInfo()
    }

    private fun getMockInfo() {
        Timber.d("getMockInfo")
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(2000)
            try {
                val guardsList = async { getGuardsListUseCase.getGuards(_state.value.networkType) }
                val requestsList =
                    async { if (_state.value.networkType == NetworkType.ON_GUARD) getGuardsRequestsUseCase.getGuardRequests() else emptyList() }
                _state.update {
                    it.copy(
                        data = NetworkMainModel(
                            requestsList = requestsList.await(),
                            guardiansList = mapToItems(guardsList.await()),
                        )
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(errorText = e.localizedMessage) }
            }.also {
                _state.update { it.copy(isLoading = false) }
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
        _state.value.data?.let { sData ->
            _state.update { it.copy(data = sData.copy(searchText = searchString)) }
            mTextSearch.value = searchString
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
            _state.update {
                it.copy(
                    data = _state.value.data?.copy(searchScreenSectionModel = contactsModel)?.copy()
                )
            }

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
        val data = _state.value.data
        data?.let { d ->
            _state.update {
                it.copy(
                    data = d.copy(searchScreenSectionModel = SearchScreenSectionModel(result))
                )
            }
        }
    }

    fun checkNavBarStatus() {
        Timber.i("onResume ${_state.value.screenState}")
        Handler(Looper.getMainLooper()).postDelayed({
            navigateRoute(NetworkMainRoute.IsNavBarVisible(_state.value.screenState == NetworkScreen.MAIN))
        }, 100)

    }
}