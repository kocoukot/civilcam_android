package com.civilcam.domainLayer

enum class ServerErrors(val errorCode: Int) {
    SOME_ERROR(0),
    ACCESS_DENIED(1),
    WRONG_APP_KEY(3),
    INTERNAL_SERVER_ERROR(4),
    WRONG_INPUT(5),
    USER_NOT_FOUND_BY_EMAIL(6),
    USER_NOT_FOUND(7),
    INCORRECT_EMAIL_OR_PASSWORD(8),
    EMAIL_ALREADY_REGISTERED(9),
    USER_CREDENTIAL_NOT_FOUND(10),
    EMAIL_ALREADY_VERIFIED(11),
    PASSWORD_NOT_MATCH(12),
    PIN_CODE_NOT_MATCH(13),
    OTP_CODE_EXPIRED(14),
    WRONG_PASSWORD_RECOVERY_TOKEN(15),
    PASSWORD_RECOVERY_TOKEN_EXPIRED(16),
    USER_EMAIL_IS_NOT_VERIFIED(17),
    ACCOUNT_INACTIVE(18),
    EMAIL_WRONG(19),
    PHONE_NUMBER_WRONG(20),
    PHONE_NUMBER_ALREADY_USED(21),
    PHONE_NUMBER_ALREADY_VERIFIED(22),
    IMAGE_FILE_REQUIRED(23),
    WRONG_IMAGE_FORMAT(24),
    FILE_TOO_LARGE(25),
    USER_AGE_WRONG(26),
    NOTIFICATION_NOT_FOUND(27),
    INVITE_YOURSELF(28),
    GUARD_REQUEST_ALREADY_EXISTS(29),
    NO_GUARDIANS_AVAILABLE(30),
    ALERT_NOT_FOUND(31),
    NO_INTERNET_CONNECTION(9999),
    ;


    companion object {
        fun byCode(code: Int?) = values().find { it.errorCode == code } ?: SOME_ERROR
    }
}

