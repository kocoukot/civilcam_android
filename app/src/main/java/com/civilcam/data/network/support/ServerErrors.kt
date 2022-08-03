package com.civilcam.data.network.support

import com.squareup.moshi.Json

enum class ServerErrors(val errorCode: Int) {
    @Json(name = "0")
    SOME_ERROR(0),
    @Json(name = "1")
    ACCESS_DENIED(1),
    @Json(name = "2")
    WRONG_APP_KEY(2),
    @Json(name = "3")
    INTERNAL_SERVER_ERROR(3),
    @Json(name = "4")
    WRONG_INPUT(4),
    @Json(name = "5")
    USER_NOT_FOUND_BY_EMAIL(5),
    @Json(name = "6")
    USER_NOT_FOUND(6),
    @Json(name = "33")
    INCORRECT_EMAIL_OR_PASSWORD(33),
    @Json(name = "8")
    EMAIL_ALREADY_REGISTERED(8),
    @Json(name = "9")
    USER_CREDENTIAL_NOT_FOUND(9),
    @Json(name = "10")
    EMAIL_ALREADY_VERIFIED(10),
    @Json(name = "11")
    PASSWORD_NOT_MATCH(11),
    @Json(name = "12")
    OTPCODE_EXPIRED(12),
    @Json(name = "13")
    WRONG_PASSWORD_RECOVERY_TOKEN(13),
    @Json(name = "14")
    PASSWORD_RECOVERY_TOKEN_EXPIRED(14),
    @Json(name = "15")
    USER_EMAIL_IS_NOT_VERIFIED(15),
    @Json(name = "16")
    ACCOUNT_INACTIVE(16),
    @Json(name = "17")
    IMAGE_FILE_REQUIRED(17),
    @Json(name = "18")
    WRONG_IMAGE_FORMAT(18),
    @Json(name = "19")
    FILE_TOO_LARGE(19),
    @Json(name = "20")
    EMAIL_WRONG(20),
    @Json(name = "21")
    NOT_OWNER_OF_ASSIGNMENT_OR_ACCEPTED(21),
    @Json(name = "22")
    ASSIGNMENT_NOT_FOUND(22),
    @Json(name = "23")
    NEWS_DELETED_OR_ACCEPTED(23),
    @Json(name = "24")
    SUBMISSION_NOT_FOUND(24),
    @Json(name = "25")
    EXCEEDED_LIMIT_OF_FILES(25),
    @Json(name = "26")
    NEW_PASSWORD_EQUAL_CURRENT(26),
    @Json(name = "27")
    NEWS_CHANNEL_NOT_FOUND(27),
    @Json(name = "46")
    USERNAME_ALREADY_REGISTERED(46),
    @Json(name = "37")
    STAFF_NOT_FOUND(37),
    @Json(name = "9999")
    NO_INTERNET_CONNECTION(9999);


    companion object {
        fun byCode(code: Int?) = values().find { it.errorCode == code } ?: SOME_ERROR
    }
}


