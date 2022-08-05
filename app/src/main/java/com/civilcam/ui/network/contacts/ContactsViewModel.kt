package com.civilcam.ui.network.contacts

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.data.local.ContactsStorage
import com.civilcam.data.local.model.Contact
import com.civilcam.data.local.model.PersonContactFilter
import com.civilcam.ui.network.contacts.model.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.*


@OptIn(FlowPreview::class)
class ContactsViewModel(
    private val contactsStorage: ContactsStorage
) : ComposeViewModel<ContactsState, ContactsRoute, ContactsActions>() {
    override var _state: MutableStateFlow<ContactsState> = MutableStateFlow(ContactsState())

    private val _textSearch = MutableStateFlow("")
    private val textSearch: StateFlow<String> = _textSearch.asStateFlow()

    init {
        viewModelScope.launch {
            textSearch.debounce(400).collect { query ->
                val contactList =
                    contactsStorage.getContacts(PersonContactFilter()).sortedBy { it.name }.filter {
                        it.name.contains(query, ignoreCase = true) || it.phoneNumber.contains(query)
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
        _textSearch.value = searchString
    }

    private fun inviteContact(contact: PersonContactItem) {
        val contactsModel = _state.value.data?.contactsList ?: mutableListOf()
        contactsModel.let { contacts ->
            (contacts.find { it is PersonContactItem && it.name == contact.name && it.phoneNumber == contact.phoneNumber } as PersonContactItem).isInvited =
                true
        }
        _state.value = _state.value.copy(data = ContactsModel(contactsModel))
    }


    fun fetchContacts() {
        viewModelScope.launch {
            val contacts = contactsStorage.getContacts(PersonContactFilter()).sortedBy { it.name }
            mapToItems(contacts)
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
        _state.value = _state.value.copy(data = ContactsModel(items))
    }

}


    



