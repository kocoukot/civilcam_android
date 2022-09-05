package com.civilcam.domainLayer.model

enum class ButtonAnswer(val answer: Boolean, val domain: String) {
    ACCEPT(true, "accepted"),
    DECLINE(false, "declined");
}