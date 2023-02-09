package com.civilcam.ext_features.alert

import android.content.Context

interface AlertDialogType {
    fun title(context: Context): String
    fun text(context: Context, vararg args: String): String
    fun alertButtons(): AlertDialogButtons = AlertDialogButtons.OK

    fun errorText(): String = ""


    open class Base(
        private val title: Int = 0,
        private val text: Int = 0,
        private val alertButtons: AlertDialogButtons = AlertDialogButtons.OK,
        private val errorText: String = ""
    ) : AlertDialogType {
        override fun title(context: Context): String =
            if (title != 0) context.getString(title) else ""

        override fun text(context: Context, vararg args: String): String =
            if (text != 0) {
                if (args.isNotEmpty()) context.getString(text, *args) else context.getString(text)
            } else errorText

        override fun alertButtons(): AlertDialogButtons = alertButtons

        override fun errorText() = errorText

    }

    object Empty : AlertDialogType {
        override fun title(context: Context): String = ""

        override fun text(context: Context, vararg args: String): String = ""

    }

    class ErrorAlert(private val _errorText: String) : Base(errorText = _errorText)
}


