package com.civilcam.ext_features.ext

import com.civilcam.domainLayer.model.user.LanguageType

interface LanguageCheck {

    fun checkLanguage()

    fun setLanguage(language: LanguageType)

}
