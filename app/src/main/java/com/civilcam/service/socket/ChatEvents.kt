package com.digi_crony.service.socket

enum class ChatEvents(val msgType: String) {
    INCOME_MESSAGE("chatMessage"),
    INCOME_MESSAGE_ACK("chatMessageAck"),

    INCOME_MESSAGES_PAGE("messagesPage"),
    INCOME_CHAT_MESSAGE_ACK("chatMessageAck"),
    INCOME_PLACE_DETAILS("placeDetails"),
    OUTCOME_PLACE_DETAILS("getPlaceDetails"),

    INIT_CONVERSATION("initConversation")
}