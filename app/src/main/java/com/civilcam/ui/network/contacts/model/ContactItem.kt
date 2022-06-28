package com.civilcam.ui.network.contacts.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ContactItem(val type: Int) {

    companion object {
        const val ITEM_TYPE_PERSON = 1
        const val ITEM_TYPE_LETTER = 2
    }
}

@Parcelize
data class PersonContactItem(
    val name: String,
    val phoneNumber: String,
    val avatar: Uri,
    var isInvited: Boolean = false
) : ContactItem(ITEM_TYPE_PERSON), Parcelable

class LetterContactItem(val letter: String) : ContactItem(ITEM_TYPE_LETTER)