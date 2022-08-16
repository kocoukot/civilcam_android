package com.civilcam.data.network

object Endpoint {

    object Auth {
        const val CHECK_USER = "check-user-exists"
        const val SIGN_UP = "sign-up"
    }

    object Verification {
        const val VERIFY_OTP_CODE = "verify-otp-code"
        const val RESEND_OTP_CODE = "resend-otp-code"
    }

    object User {
        const val CURRENT_USER = "current-user"
        const val ACCEPT_TERMS_POLICY = "accept-terms-policy"
        const val LOGOUT = "logout"

    }

    object Profile {
        const val GET_USER_PROFILE = "get-user-profile"
        const val SET_USER_PROFILE = "set-user-profile"
        const val UPDATE_USER_PROFILE = "update-user-profile"
        const val CHANGE_USER_PHONE = "change-user-phone"
        const val SET_USER_AVATAR = "set-user-avatar"
        const val DELETE_USER_AVATAR = "delete-user-avatar"
    }


    object Subscriptions {
        const val GET_SUBSCRIPTIONS_LIST = "get-subscriptions-list"
    }

    object Public {
        const val LEGAL_DOCS = "legal-docs"
    }
}














