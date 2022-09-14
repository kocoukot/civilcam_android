package com.civilcam.data.network

object Endpoint {

    object Auth {
        const val CHECK_USER = "check-user-exists"
        const val SIGN_UP = "sign-up"
        const val SIGN_IN = "sign-in"
        const val RESET_PASSWORD = "reset-password"
        const val VERIFY_RESET_PASSWORD_OTP = "verify-reset-password-otp"
        const val RECOVER_PASSWORD = "recover-password"
        const val GOOGLE_SIGN_IN = "google-sign-in"
        const val FACEBOOK_SIGN_IN = "facebook-sign-in"
    }

    object Verification {
        const val VERIFY_OTP_CODE = "verify-otp-code"
        const val RESEND_OTP_CODE = "resend-otp-code"
    }

    object User {
        const val CURRENT_USER = "current-user"
        const val ACCEPT_TERMS_POLICY = "accept-terms-policy"
        const val LOGOUT = "logout"
        const val CHANGE_EMAIL = "change-email"
        const val CHECK_PASSWORD = "check-password"
        const val CHANGE_PASSWORD = "change-password"
        const val SET_USER_LANGUAGE = "set-user-language"
        const val DELETE_ACCOUNT = "delete-account"
        const val CONTACT_SUPPORT = "contact-support"
        const val TOGGLE_SETTINGS = "toggle-settings"
        const val SET_FCM_TOKEN = "set-fcm-token"
        const val CHECK_PIN = "check-pin"
        const val SET_PIN = "set-pin"
    }

    object Profile {
        const val GET_USER_PROFILE = "get-user-profile"
        const val SET_USER_PROFILE = "set-user-profile"
        const val UPDATE_USER_PROFILE = "update-user-profile"
        const val CHANGE_USER_PHONE = "change-user-phone"
        const val SET_USER_AVATAR = "set-user-avatar"
        const val DELETE_USER_AVATAR = "delete-user-avatar"
    }

    object Guardians {
        const val NETWORK = "network"
        const val SEARCH = "search"
        const val INVITES = "invites"
        const val INVITE_BY_PHONE = "invite-by-phone"
        const val REQUESTS = "requests"
        const val PERSON = "person"
        const val ASK_TO_GUARD = "ask-to-guard"
        const val SET_REQUEST_REACTION = "set-request-reaction"
        const val ON_GUARD = "on-guard"
        const val GUARDIANS = "guardians"
        const val DELETE_GUARDIAN = "guardian"
        const val STOP_GUARDING = "stop-guarding"
    }

    object Subscriptions {
        const val GET_SUBSCRIPTIONS_LIST = "get-subscriptions-list"
    }

    object Public {
        const val LEGAL_DOCS = "legal-docs"
    }

    object Google {
        const val OAUTH_V4_TOKEN = "oauth2/v4/token"
    }
}