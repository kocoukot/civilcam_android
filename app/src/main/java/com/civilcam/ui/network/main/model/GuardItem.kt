package com.civilcam.ui.network.main.model

import android.os.Parcelable
import com.civilcam.domain.model.guard.GuardianStatus
import kotlinx.parcelize.Parcelize

sealed class GuardItem(val type: Int) {

    companion object {
        const val ITEM_TYPE_PERSON = 1
        const val ITEM_TYPE_LETTER = 2
    }
}

@Parcelize
data class GuardianItem(
    val guardianId: Int,
    val guardianName: String,
    val guardianAvatar: Int,
    val guardianStatus: GuardianStatus,
) : GuardItem(ITEM_TYPE_PERSON), Parcelable
//
//@Parcelize
//data class PersonGuardItem(
//    val name: String,
//    val phoneNumber: String,
//    val avatar: Uri,
//    var isInvited: Boolean = false
//) : GuardItem(ITEM_TYPE_PERSON), Parcelable

class LetterGuardItem(val letter: String) : GuardItem(ITEM_TYPE_LETTER)