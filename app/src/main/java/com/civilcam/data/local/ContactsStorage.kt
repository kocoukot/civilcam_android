package com.civilcam.data.local

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.civilcam.data.local.model.Contact
import com.civilcam.data.local.model.ContactFilter


class ContactsStorage(private val context: Context) {

    suspend fun getContacts(contactFilter: ContactFilter): List<Contact> {
        val contacts = mutableListOf<Contact>()
        queryContacts { cursor ->
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        .orEmpty()
                val phoneNumber =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        .takeIf { it > 0 }
                        ?.let {
                            val contactId =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                            queryPhoneNumber(contactId) { phoneCursor ->
                                phoneCursor
                                    .takeIf { it.moveToNext() }
                                    ?.let {
                                        phoneCursor.getString(
                                            phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                        )
                                    }
                            }
                        }
                        .orEmpty()
                val avatar =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                        .orEmpty()
                Contact(name, phoneNumber, avatar)
                    .takeIf { contactFilter.filter(it) }
                    ?.also { contacts.add(it) }
            }
        }
        return contacts
    }

    private fun queryContacts(use: (Cursor) -> Unit) {
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        )
        context.contentResolver
            .query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                null
            )
            ?.use(use)
    }

    private fun queryPhoneNumber(contactId: String, use: (Cursor) -> String?): String? {
        return context.contentResolver
            .query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                arrayOf(contactId),
                null
            )
            ?.use(use)
    }
}