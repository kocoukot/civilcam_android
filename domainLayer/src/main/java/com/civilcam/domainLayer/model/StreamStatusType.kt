package com.civilcam.domainLayer.model

enum class StreamStatusType(val type: String) {
	ACTIVE("active"), IDLE("idle"), DISABLED("disabled");
	
	companion object {
		fun byDomain(domain: String) = values().find { it.type.equals(domain, true) } ?: ACTIVE
	}
}