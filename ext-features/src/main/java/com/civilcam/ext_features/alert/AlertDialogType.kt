package com.civilcam.ext_features.alert

interface AlertDialogType {
    fun title(): String
    fun text(): String
    fun alertButtons(): AlertDialogButtons = AlertDialogButtons.OK

    open class Base(
        private val title: String = "",
        private val text: String,
        private val alertButtons: AlertDialogButtons = AlertDialogButtons.OK
    ) : AlertDialogType {
        override fun title(): String = title

        override fun text(): String = text

        override fun alertButtons(): AlertDialogButtons = alertButtons

    }

    object Empty : AlertDialogType {
        override fun title(): String = ""
        override fun text(): String = ""
    }

    class ErrorAlert(private val _errorText: String) : Base(text = _errorText)
}
