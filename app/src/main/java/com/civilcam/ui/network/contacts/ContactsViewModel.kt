package com.civilcam.ui.network.contacts

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.serviceCast
import com.civilcam.data.local.ContactsStorage
import com.civilcam.data.local.model.Contact
import com.civilcam.data.local.model.PersonContactFilter
import com.civilcam.domainLayer.usecase.guardians.InviteByNumberUseCase
import com.civilcam.ui.common.ext.SearchQuery
import com.civilcam.ui.network.contacts.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class ContactsViewModel(
    private val contactsStorage: ContactsStorage,
    private val inviteByNumberUseCase: InviteByNumberUseCase
) : ComposeViewModel<ContactsState, ContactsRoute, ContactsActions>(), SearchQuery {
    override var _state: MutableStateFlow<ContactsState> = MutableStateFlow(ContactsState())
    override val mTextSearch = MutableStateFlow("")

    init {
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

    override fun setInputActions(action: ContactsActions) {
        when (action) {
            ContactsActions.ClickGoBack -> goBack()
            ContactsActions.ClickGoInviteByNumber -> moveToInvite()
            is ContactsActions.ClickInvite -> inviteContact(action.contact)
            is ContactsActions.ClickSearch -> searchContact(action.searchString)
        }
    }

    private fun goBack() {
        navigateRoute(ContactsRoute.GoBack)
    }

    private fun moveToInvite() {
        navigateRoute(ContactsRoute.GoInviteByNumber)
    }

    private fun searchContact(searchString: String) {
        mTextSearch.value = searchString
    }

    private fun inviteContact(contact: PersonContactItem) {
        _state.update { it.copy(isLoading = true) }
        networkRequest(
            action = { inviteByNumberUseCase.invoke(contact.phoneNumber) },
            onSuccess = {
                val contactsModel = _state.value.data?.contactsList ?: mutableListOf()
                contactsModel.let { contacts ->
                    (contacts.find { it is PersonContactItem && it.name == contact.name && it.phoneNumber == contact.phoneNumber } as PersonContactItem).isInvited =
                        true
                }
                _state.update { it.copy(data = ContactsModel(contactsModel)) }
            },
            onFailure = { error ->
                error.serviceCast { errorMessage, _, isForceLogout ->
                    _state.update { it.copy(errorText = errorMessage) }
                }
            },
            onComplete = { _state.update { it.copy(isLoading = false) } }
        )
    }


    fun fetchContacts() {
        viewModelScope.launch {
            contactsStorage.getContacts(PersonContactFilter()).sortedBy { it.name }.let {
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
            groupedContacts.getOrPut(charKey) { mutableListOf() }
                .add(PersonContactItem(it.name, it.phoneNumber, it.avatar.toUri()))
        }
        groupedContacts.forEach { (key, value) ->
            items.apply {
                add(LetterContactItem(key.toString()))
                addAll(value)
            }
        }
        _state.update { it.copy(data = ContactsModel(items)) }
    }

}