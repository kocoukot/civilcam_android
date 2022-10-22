package com.civilcam.domainLayer.model.user

enum class UserState(val domain: String) {
    alert("alert"),
    safe("safe");

    companion object {
        fun byDomain(domain: String) = values().find { it.domain.equals(domain, true) } ?: safe

        fun resolveState(isSos: Boolean) = if (isSos) alert else safe
    }
}