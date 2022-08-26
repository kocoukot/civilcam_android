package com.civilcam.utils

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.civilcam.CivilcamApplication.Companion.instance
import com.civilcam.domainLayer.model.user.LanguageType
import java.util.*


object LocaleHelper {
    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    // the method is used to set the language at runtime
    fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        return updateResources(context, language)
    }

    fun getSelectedLanguage(): LanguageType {
        val sharedPref = instance.getSharedPreferences(SELECTED_LANGUAGE, Context.MODE_PRIVATE)
        val value = sharedPref.getString(SELECTED_LANGUAGE, "en") ?: "en"
        return LanguageType.byValue(value)
    }

    private fun persist(context: Context, language: String) {
        val sharedPref = context.getSharedPreferences(SELECTED_LANGUAGE, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        val resources = context.resources
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context.createConfigurationContext(configuration)
    }


    @Composable
    fun SetLanguageCompose(lang: LanguageType) {
        LocaleHelper.setLocale(LocalContext.current, lang.langValue)
        val locale = Locale(lang.langValue)
        val configuration = LocalConfiguration.current
        configuration.setLocale(locale)
        val resources = LocalContext.current.resources
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}