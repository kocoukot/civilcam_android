package com.civilcam.data.network.support

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
    OTP_CODE_EXPIRED(13),
    WRONG_PASSWORD_RECOVERY_TOKEN(14),
    PASSWORD_RECOVERY_TOKEN_EXPIRED(15),
    USER_EMAIL_IS_NOT_VERIFIED(16),
    ACCOUNT_INACTIVE(17),
    EMAIL_WRONG(18),
    PHONE_NUMBER_WRONG(19),
    PHONE_NUMBER_ALREADY_USED(20),
    PHONE_NUMBER_ALREADY_VERIFIED(21),
    IMAGE_FILE_REQUIRED(22),
    WRONG_IMAGE_FORMAT(23),
    FILE_TOO_LARGE(24),
    USER_AGE_WRONG(25),
    NOTIFICATION_NOT_FOUND(26),
    NO_INTERNET_CONNECTION(9999),
    ;


    companion object {
        fun byCode(code: Int?) = values().find { it.errorCode == code } ?: SOME_ERROR
    }
}





































































