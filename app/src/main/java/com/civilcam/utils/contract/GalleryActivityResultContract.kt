package com.civilcam.utils.contract

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class GalleryActivityResultContract : ActivityResultContract<Unit, Uri>() {

    override fun createIntent(context: Context, input: Unit): Intent =
        Intent().apply {
            type = INTENT_TYPE_IMAGES
            action = Intent.ACTION_GET_CONTENT
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri =
        (intent?.data ?: Uri.parse(""))

    companion object {
        private const val INTENT_TYPE_IMAGES = "image/*"
    }


}