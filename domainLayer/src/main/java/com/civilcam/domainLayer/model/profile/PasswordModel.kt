package com.civilcam.domainLayer.model.profile

data class PasswordModel(
    val password: String = "",
    val confirmPassword: String = "",
    val meetCriteria: Boolean = false,
) {

    val isFilled: Boolean
        get() = password == confirmPassword &&
                password.isNotEmpty() &&
                confirmPassword.isNotEmpty() &&
                meetCriteria

    val noMatch: Boolean
        get() = (password.isNotEmpty() && confirmPassword.isNotEmpty()) && (password != confirmPassword)
}