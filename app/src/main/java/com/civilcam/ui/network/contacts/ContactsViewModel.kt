package com.civilcam.ui.network.contacts

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.data.local.ContactsStorage
import com.civilcam.data.local.model.PersonContactFilter
import com.civilcam.ui.network.contacts.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*


class ContactsViewModel(
    private val contactsStorage: ContactsStorage
) : ComposeViewModel<ContactsState, ContactsRoute, ContactsActions>() {
    override var _state: MutableStateFlow<ContactsState> = MutableStateFlow(ContactsState())

    override fun setInputActions(action: ContactsActions) {
        when (action) {
            ContactsActions.ClickGoBack -> goBack()
            ContactsActions.ClickGoInviteByNumber -> moveToInvite()
            is ContactsActions.ClickInvite -> inviteContact(action.contact)
        }
    }

    private fun goBack() {
        _steps.value = ContactsRoute.GoBack
    }

    private fun moveToInvite() {
        _steps.value = ContactsRoute.GoInviteByNumber
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
            val items = mutableListOf<ContactItem>()
            val contacts = contactsStorage.getContacts(PersonContactFilter()).sortedBy { it.name }

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
}


    



