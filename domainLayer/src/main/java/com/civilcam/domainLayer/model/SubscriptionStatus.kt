package com.civilcam.domainLayer.model

enum class SubscriptionStatus(val type: String) {
	active("active"),
	disabled("disabled"),
	deleted("deleted");
}