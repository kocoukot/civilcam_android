package com.civilcam.domainLayer.model.alerts

enum class AlertStatus(val domain: String) {
    ACTIVE("active"),
    RESOLVED("resolved"),
    DELETED("deleted");

    companion object {
        fun byDomain(domain: String) = values().find { it.domain.equals(domain, true) } ?: RESOLVED
    }

}