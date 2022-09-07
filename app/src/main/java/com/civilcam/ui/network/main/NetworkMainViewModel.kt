package com.civilcam.ui.network.main

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.serviceCast
import com.civilcam.data.network.support.ServiceException
import com.civilcam.di.source.KoinInjector
import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.model.guard.*
import com.civilcam.domainLayer.usecase.guardians.*
import com.civilcam.domainLayer.usecase.user.GetLocalCurrentUserUseCase
import com.civilcam.ui.common.ext.SearchQuery
import com.civilcam.ui.network.main.model.*
import com.civilcam.ui.network.source.SearchGuardiansDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.util.*


class NetworkMainViewModel(
    injector: KoinInjector,
    private val screen: NetworkScreen = NetworkScreen.MAIN,
    private val getGuardsListUseCase: GetGuardsListUseCase,
    private val getGuardsRequestsUseCase: GetGuardsRequestsUseCase,
    private val askToGuardUseCase: AskToGuardUseCase,
    private val getUserNetworkUseCase: GetUserNetworkUseCase,
    private val getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase,
    private val setRequestReactionUseCase: SetRequestReactionUseCase,
    private val getNetworkRequestsUseCase: GetNetworkRequestsUseCase
) : ComposeViewModel<NetworkMainState, NetworkMainRoute, NetworkMainActions>(), SearchQuery,
    KoinInjector by injector {
    override var _state: MutableStateFlow<NetworkMainState> = MutableStateFlow(NetworkMainState())
    override val mTextSearch = MutableStateFlow("")
    var searchList = loadRPlacesList()

    init {
        _state.update { it.copy(screenState = screen) }
        Timber.i("Screen type $screen")
        checkNavBarStatus()
        getLocalCurrentUserUseCase().let { user ->
            _state.update { it.copy(userAvatar = user.userBaseInfo.avatar) }
        }
        query(viewModelScope) { query ->
            query.let {
                viewModelScope.launch {
                    try {
                        _state.update { it.copy(refreshList = Unit) }
                    } catch (e: Exception) {
                        _state.update { it.copy(errorText = e.localizedMessage) }
                    }
                }
            }
        }
        fetchGuardsList()
    }

    private fun fetchGuardsList() {
        _state.update { it.copy(isLoading = true) }
        networkRequest(
            action = { getUserNetworkUseCase(_state.value.networkType) },
            onSuccess = { data ->
                Timber.i("userdata $data")
                _state.update {
                    it.copy(
                        data = NetworkMainModel(
                            requestsList = data.requestsList,
                            guardiansList = mapToItems(data.guardiansList),
                            onGuardList = mapToItems(data.onGuardList)
                        )
                    )
                }
            },
            onFailure = { error ->
                error.serviceCast { msg, _, isForceLogout -> _state.update { it.copy(errorText = msg) } }
            },
            onComplete = { _state.update { it.copy(isLoading = false) } },
        )
    }

    fun stopRefresh() {
        _state.update { it.copy(refreshList = null) }
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
            NetworkMainActions.ClickGoSearch -> goSearchGuard()
            NetworkMainActions.ClickGoContacts -> goContacts()

            is NetworkMainActions.EnteredSearchString -> searchContact(action.searchQuery)
            is NetworkMainActions.ClickAddUser -> addUser(action.user)
            NetworkMainActions.ClearErrorText -> clearErrorText()
            is NetworkMainActions.SetRequestReaction -> setUserReaction(
                action.user,
                action.reaction
            )
        }
    }

    private fun setUserReaction(user: GuardianItem, reaction: ButtonAnswer) {
        _state.update { it.copy(isLoading = true) }
        networkRequest(
            action = {
                setRequestReactionUseCase(reaction, user.statusId)
            },
            onSuccess = { loadRequestsList() },
            onFailure = { error ->
                error.serviceCast { msg, _, isForceLogout -> _state.update { it.copy(errorText = msg) } }
            },
            onComplete = {
                _state.update { it.copy(isLoading = false) }
            },
        )
    }

    fun loadRequestsList() {
        if (_state.value.screenState == NetworkScreen.REQUESTS) {
            _state.update { it.copy(isLoading = true) }
            networkRequest(
                action = { getNetworkRequestsUseCase() },
                onSuccess = { response ->
                    _state.update { it.copy(data = it.data.copy(requestsList = response)) }
                    if (response.isEmpty()) goBack()
                },
                onFailure = { error ->
                    error.serviceCast { msg, _, isForceLogout -> _state.update { it.copy(errorText = msg) } }
                },
                onComplete = {
                _state.update { it.copy(isLoading = false) }
            },
        )
        }
    }

    private fun clearErrorText() {
        _state.update { it.copy(errorText = "") }
    }

    private fun goBack() {
        when (_state.value.screenState) {
            NetworkScreen.MAIN -> {}
            NetworkScreen.REQUESTS -> {
                _state.update { it.copy(screenState = NetworkScreen.MAIN) }
            }
            NetworkScreen.SEARCH_GUARD, NetworkScreen.ADD_GUARD -> {
                mTextSearch.value = ""
                _state.update { it.copy(screenState = NetworkScreen.MAIN) }
            }
        }
        checkNavBarStatus()
        fetchGuardsList()
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

    private fun goSearchGuard() {
        searchList = loadRPlacesList()
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
        fetchGuardsList()
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
                        statusId = it.statusId
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
        Timber.tag("networkSearch").i("searchString $searchString")
        _state.update { it.copy(data = _state.value.data.copy(searchText = searchString)) }
        mTextSearch.value = searchString
    }

    private fun addUser(user: PersonModel) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            kotlin.runCatching { askToGuardUseCase(user.personId) }
                .onSuccess {
                    var contactsModel =
                        _state.value.data.searchScreenSectionModel
                    contactsModel =
                        contactsModel.copy(pendingList = contactsModel.pendingList + listOf(user.personId))
                    _state.update {
                        it.copy(
                            data = _state.value.data.copy(searchScreenSectionModel = contactsModel)
                        )
                    }
                }
                .onFailure { error ->
                    error.serviceCast { msg, _, isForceLogout -> _state.update { it.copy(errorText = msg) } }
                }
                .also {
                    _state.update { it.copy(isLoading = false) }
                }

        }
    }

    fun checkNavBarStatus() {
        Timber.i("onResume ${_state.value.screenState}")
        Handler(Looper.getMainLooper()).postDelayed({
            navigateRoute(NetworkMainRoute.IsNavBarVisible(_state.value.screenState == NetworkScreen.MAIN))
        }, 100)

    }

    private fun loadRPlacesList(): Flow<PagingData<PersonModel>> {
        return Pager(
            config = PagingConfig(pageSize = 40, initialLoadSize = 20, prefetchDistance = 6),
            pagingSourceFactory = {
                koin.get<SearchGuardiansDataSource> { parametersOf(mTextSearch.value) }
            }
        ).flow
            .cachedIn(viewModelScope)
    }

    fun resolveSearchError(error: ServiceException) {
        if (error.isForceLogout) navigateRoute(NetworkMainRoute.ForceLogout)
    }
}