package com.civilcam.domainLayer.model

enum class LanguageType(val langTitle: String, val langValue: String) {
    ENGLISH("English", "en"),
    SPAIN("Español", "es");


    companion object {
        fun byValue(langValue: String) = values()
            .find { it.langValue.equals(langValue, true) } ?: ENGLISH
    }
}