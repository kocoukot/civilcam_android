package com.civilcam.ui.network.contacts

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.civilcam.data.local.ContactsStorage
import com.civilcam.data.local.model.Contact
import com.civilcam.data.local.model.PersonContactFilter
import com.civilcam.domainLayer.usecase.guardians.GetPhoneInvitesUseCase
import com.civilcam.domainLayer.usecase.guardians.InviteByNumberUseCase
import com.civilcam.ext_features.SearchQuery
import com.civilcam.ext_features.arch.BaseViewModel
import com.civilcam.ext_features.compose.ComposeFragmentActions
import com.civilcam.ext_features.ext.clearPhone
import com.civilcam.ui.network.contacts.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class ContactsViewModel(
    private val phonesList: List<String>,
    private val contactsStorage: ContactsStorage,
    private val inviteByNumberUseCase: InviteByNumberUseCase,
    private val getPhoneInvitesUseCase: GetPhoneInvitesUseCase
) : BaseViewModel.Base<ContactsState>(
    mState = MutableStateFlow(ContactsState())
),
    SearchQuery {

    override val mTextSearch = MutableStateFlow("")

    init {
        Timber.tag("civil_my_contacts").d("civil_contacts $phonesList")
        viewModelScope.launch {
            updateInfo { copy(isLoading = true) }
            networkRequest(
                action = { getPhoneInvitesUseCase() },
                onSuccess = { list -> updateInfo { copy(invitesList = list) } },
                onFailure = { error -> updateInfo { copy(errorText = error) } },
                onComplete = { updateInfo { copy(isLoading = false) } },
            )
        }
        query(viewModelScope) { query ->
            viewModelScope.launch {
                val contactList =
                    contactsStorage.getContacts(PersonContactFilter())
                        .sortedBy { it.name }
                        .filter {
                            it.name.contains(query, ignoreCase = true)
                                    || it.phoneNumber.contains(query)
                        }
                mapToItems(contactList)
            }
        }
    }

    override fun setInputActions(action: ComposeFragmentActions) {
        when (action) {
            ContactsActions.ClickGoBack -> goBack()
            ContactsActions.ClickGoInviteByNumber -> moveToInvite()
            is ContactsActions.ClickInvite -> inviteContact(action.contact)
            is ContactsActions.ClickSearch -> searchContact(action.searchString)
            ContactsActions.ClearErrorText -> clearErrorText()
        }
    }

    override fun clearErrorText() {
        updateInfo { copy(errorText = "") }
    }

    private fun goBack() {
        sendRoute(ContactsRoute.GoBack)
    }

    private fun moveToInvite() {
        sendRoute(ContactsRoute.GoInviteByNumber)
    }

    private fun searchContact(searchString: String) {
        mTextSearch.value = searchString
    }

    private fun inviteContact(contact: PersonContactItem) {
        updateInfo { copy(isLoading = true) }
        networkRequest(
            action = { inviteByNumberUseCase.invoke(contact.phoneNumber) },
            onSuccess = {
                val contactsModel = getState().contactsList ?: mutableListOf()
                contactsModel.let { contacts ->
                    (contacts.find { it is PersonContactItem && it.name == contact.name && it.phoneNumber == contact.phoneNumber } as PersonContactItem).isInvited =
                        InvitationState.PENDING
                }
                updateInfo { copy(contactsList = contactsModel) }
                Timber.tag("civil_my_contacts").d("civil_contacts has ${getState().contactsList}")

            },
            onFailure = { error -> updateInfo { copy(errorText = error) } },
            onComplete = { updateInfo { copy(isLoading = false) } }
        )
    }


    fun fetchContacts() {
        viewModelScope.launch {
            contactsStorage.getContacts(PersonContactFilter())
                .sortedBy { it.name }
                .let {
                    mapToItems(it)
                }
        }
    }

    private fun mapToItems(contacts: List<Contact>) {
        val items = mutableListOf<ContactItem>()
        val groupedContacts = TreeMap<Char, MutableList<PersonContactItem>> { key1, key2 ->
            key1.compareTo(key2)
        }
        contacts.forEach {
            val charKey = if (it.name.first().isLetter()) it.name.first() else '#'
            Timber.tag("civil_my_contacts")
                .d("civil_contacts has ${it.phoneNumber.clearPhone().takeLast(10)}")


            groupedContacts.getOrPut(charKey) { mutableListOf() }
                .add(
                    PersonContactItem(
                        it.name,
                        it.phoneNumber,
                        it.avatar.toUri(),
                        if (it.phoneNumber.clearPhone()
                                .takeLast(10) in phonesList
                        ) InvitationState.IN_APP else InvitationState.NEW
                    )
                )
        }
        groupedContacts.forEach { (key, value) ->
            items.apply {
                add(LetterContactItem(key.toString()))
                addAll(value)
            }
        }

        Timber.tag("civil_my_contacts").d("civil_contacts $items")

        updateInfo { copy(contactsList = items) }
    }

}