package com.civilcam.data.local.model

interface ContactFilter {

    fun filter(contact: Contact): Boolean
}

class PersonContactFilter : ContactFilter {

    override fun filter(contact: Contact): Boolean =
        contact.name.isNotEmpty() && contact.phoneNumber.startsWith("+")
}
