package com.civilcam.ui.network.main

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.civilcam.data.local.ContactsStorage
import com.civilcam.data.local.model.PersonContactFilter
import com.civilcam.domainLayer.ServerErrors
import com.civilcam.domainLayer.ServiceException
import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.model.guard.*
import com.civilcam.domainLayer.usecase.guardians.*
import com.civilcam.domainLayer.usecase.user.GetLocalCurrentUserUseCase
import com.civilcam.ext_features.KoinInjector
import com.civilcam.ext_features.SearchQuery
import com.civilcam.ext_features.arch.BaseViewModel
import com.civilcam.ext_features.compose.ComposeFragmentActions
import com.civilcam.ext_features.compose.ComposeFragmentRoute
import com.civilcam.ext_features.ext.clearPhone
import com.civilcam.ui.network.main.model.*
import com.civilcam.ui.network.source.SearchGuardiansDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.util.*


class NetworkMainViewModel(
    injector: KoinInjector,
    private val screen: NetworkScreen = NetworkScreen.MAIN,
    private val askToGuardUseCase: AskToGuardUseCase,
    private val getUserNetworkUseCase: GetUserNetworkUseCase,
    private val getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase,
    private val setRequestReactionUseCase: SetRequestReactionUseCase,
    private val getNetworkRequestsUseCase: GetNetworkRequestsUseCase,
    private val contactsStorage: ContactsStorage,
    private val matchUserByPhoneUseCase: MatchUserByPhoneUseCase
) : BaseViewModel.Base<NetworkMainState>(
    mState = MutableStateFlow(NetworkMainState())
),
    SearchQuery, KoinInjector by injector {

    override val mTextSearch = MutableStateFlow("")
    var searchList = loadGuardsList()

    init {
        updateInfo { copy(screenState = screen) }

        Timber.i("Screen type $screen")
        checkNavBarStatus()


        query(viewModelScope) { query ->
            query.let {
                viewModelScope.launch {
                    try {
                        updateInfo { copy(refreshList = Unit) }
                    } catch (e: Exception) {
                        updateInfo { copy(errorText = e.localizedMessage) }
                    }
                }
            }
        }
        fetchGuardsList()
    }

    fun loadAvatar() {
        getLocalCurrentUserUseCase().let { user ->
            updateInfo { copy(userAvatar = user?.userBaseInfo?.avatar) }
        }
    }

    private fun fetchGuardsList() {
        updateInfo { copy(isLoading = true) }
        networkRequest(
            action = { getUserNetworkUseCase(getState().networkType) },
            onSuccess = { data ->
                Timber.i("userdata $data")
                updateInfo {
                    copy(
                        data = NetworkMainModel(
                            requestsList = data.requestsList,
                            guardiansList = mapToItems(data.guardiansList),
                            onGuardList = mapToItems(data.onGuardList),
                            contactsList = emptyList()
                        )
                    )
                }
            },
            onFailure = { error -> updateInfo { copy(errorText = error) } },
            onComplete = { updateInfo { copy(isLoading = false) } },
        )
    }

    fun stopRefresh() {
        updateInfo { copy(refreshList = null) }
    }

    override fun setInputActions(action: ComposeFragmentActions) {
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
            NetworkMainActions.MatchContacts -> getAndMatchContacts()
        }
    }

    fun getAndMatchContacts() {
        if (getState().screenState != NetworkScreen.ADD_GUARD) return
        if (mTextSearch.value.isNotEmpty()) return
        viewModelScope.launch {
            contactsStorage.getContacts(PersonContactFilter()).sortedBy { it.name }
                .let { contacts ->
                    Timber.tag("civil_contacts")
                        .i("contacts list ${contacts.map { it.phoneNumber }}")

                    networkRequest(
                        action = {
                            matchUserByPhoneUseCase.invoke(contacts.map { contact -> contact.phoneNumber.clearPhone() })
                        },
                        onSuccess = { result ->
                            Timber.tag("civil_contacts").d("result list $result")

                            val filtered = contacts.filter {
                                it.phoneNumber.clearPhone()
                                    .takeLast(10) in result.map { phones -> phones.phone }
                            }
                            filtered.forEach { contact ->
                                result.find {
                                    it.phone == contact.phoneNumber.clearPhone().takeLast(10)
                                }?.let {
                                    contact.contactId = it.userId
                                    contact.status = it.status
                                }
                            }
                            Timber.tag("civil_contacts").d("filtered list $filtered")
                            updateInfo {
                                copy(
                                    random = (0..100).random(),
                                    data = data.copy(
                                        contactsList = filtered,
                                        inAppContactsList = result.map { it.phone }
                                    )
                                )
                            }
                        },
                        onFailure = {},
                        onComplete = {},
                    )
                }
        }
    }

    private fun setUserReaction(user: GuardianItem, reaction: ButtonAnswer) {
        updateInfo { copy(isLoading = true) }
        networkRequest(
            action = {
                setRequestReactionUseCase(reaction, user.statusId)
            },
            onSuccess = { loadRequestsList() },
            onFailure = { error -> updateInfo { copy(errorText = error) } },
            onComplete = {
                updateInfo { copy(isLoading = false) }
            },
        )
    }

    fun loadRequestsList() {
        if (getState().screenState == NetworkScreen.REQUESTS) {
            updateInfo { copy(isLoading = true) }
            networkRequest(
                action = { getNetworkRequestsUseCase() },
                onSuccess = { response ->
                    updateInfo { copy(data = data.copy(requestsList = response)) }
                    if (response.isEmpty()) goBack()
                },
                onFailure = { error -> updateInfo { copy(errorText = error) } },
                onComplete = {
                    updateInfo { copy(isLoading = false) }
                },
            )
        }
    }


    override fun clearErrorText() {
        updateInfo { copy(errorText = "") }
    }

    private fun goBack() {
        when (getState().screenState) {
            NetworkScreen.MAIN -> {}
            NetworkScreen.REQUESTS -> {
                updateInfo { copy(screenState = NetworkScreen.MAIN) }
            }
            NetworkScreen.SEARCH_GUARD, NetworkScreen.ADD_GUARD -> {
                mTextSearch.value = ""
                updateInfo { copy(screenState = NetworkScreen.MAIN) }
            }
        }
        checkNavBarStatus()
        fetchGuardsList()
    }

    private fun goContacts() {
        sendRoute(NetworkMainRoute.GoContacts(getState().data.inAppContactsList))
    }

    private fun goMyProfile() {
        sendRoute(NetworkMainRoute.GoProfile)
    }

    private fun goUser(user: GuardianItem) {
        sendRoute(NetworkMainRoute.GoUserDetail(user.guardianId))
    }

    private fun goSearchGuard() {
        searchList = loadGuardsList()
        if (getState().screenState == NetworkScreen.MAIN) {
            updateInfo { copy(screenState = NetworkScreen.SEARCH_GUARD) }
            checkNavBarStatus()
        }
    }

    private fun addGuardian() {
        searchList = loadGuardsList()
        updateInfo { copy(screenState = NetworkScreen.ADD_GUARD) }
        checkNavBarStatus()
    }

    private fun goSettings() {
        sendRoute(NetworkMainRoute.GoSettings)
    }

    private fun goRequests() {
        updateInfo { copy(screenState = NetworkScreen.REQUESTS) }
        checkNavBarStatus()
    }

    private fun changeAlertType(networkType: NetworkType) {
        updateInfo { copy(networkType = networkType) }
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
        updateInfo {
            copy(
                data = data.copy(
                    searchText = searchString,
                    contactsList = emptyList()
                )
            )
        }
        mTextSearch.value = searchString
    }

    private fun addUser(user: PersonModel) {
        viewModelScope.launch {
            updateInfo { copy(isLoading = true) }

            networkRequest(
                action = { askToGuardUseCase(user.personId) },
                onSuccess = {
                    var contactsModel =
                        getState().data.searchScreenSectionModel
                    contactsModel =
                        contactsModel.copy(pendingList = contactsModel.pendingList + listOf(user.personId))
                    updateInfo {
                        copy(
                            data = getState().data.copy(searchScreenSectionModel = contactsModel)
                        )
                    }
                },
                onFailure = { error ->
                    updateInfo { copy(errorText = error) }
                },
                onComplete = {
                    updateInfo { copy(isLoading = false) }
                },
            )
        }
    }

    fun checkNavBarStatus() {
        Timber.i("onResume ${getState().screenState}")
        Handler(Looper.getMainLooper()).postDelayed({
            sendRoute(NetworkMainRoute.IsNavBarVisible(getState().screenState == NetworkScreen.MAIN))
        }, 100)

    }

    private fun loadGuardsList(): Flow<PagingData<PersonModel>> {
        return Pager(
            config = PagingConfig(pageSize = 40, initialLoadSize = 20, prefetchDistance = 6),
            pagingSourceFactory = {
                koin.get<SearchGuardiansDataSource> { parametersOf(mTextSearch.value) }
            }
        ).flow
            .cachedIn(viewModelScope)
    }

    fun resolveSearchError(error: ServiceException) {
        if (error.isForceLogout) sendRoute(ComposeFragmentRoute.ForceLogout)
        if (error.errorCode == ServerErrors.SUBSCRIPTION_NOT_FOUND) sendRoute(ComposeFragmentRoute.SubEnd)
        else updateInfo { copy(errorText = error.errorMessage) }
    }
}