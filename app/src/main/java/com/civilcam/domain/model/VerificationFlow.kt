package com.civilcam.domain.model

enum class VerificationFlow(val rawValue: String) {
	NEW_EMAIL("NEW_EMAIL"),
	PHONE("PHONE"),
	RESET_PASSWORD("RESET_PASSWORD"),
}