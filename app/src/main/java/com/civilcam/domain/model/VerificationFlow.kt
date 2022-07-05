package com.civilcam.domain.model

enum class VerificationFlow(val rawValue: String) {
	NEW_EMAIL("NEW_EMAIL"),
	NEW_PHONE("NEW_PHONE"),
	CHANGE_EMAIL("CHANGE_EMAIL"),
	CHANGE_PHONE("CHANGE_PHONE"),
	RESET_PASSWORD("RESET_PASSWORD"),
}