package com.civilcam.domainLayer.model.guard

enum class GuardianStatus(val domain: String) {
    NEW("new"),
    PENDING("pending"),
    DECLINED("declined"),
    ACCEPTED("accepted"),
    NEED_HELP("alert"),
    SAFE("safe");


    companion object {
        fun byDomain(domain: String): GuardianStatus =
            values().find { it.domain.equals(domain, true) } ?: NEW
    }
}