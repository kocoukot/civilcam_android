package com.civilcam.domainLayer.model.user

enum class UserState(val domain: String) {
    ALERT("alert"),
    SAFE("safe");

    companion object {
        fun byDomain(domain: String) = values().find { it.domain.equals(domain, true) } ?: SAFE
    }
}