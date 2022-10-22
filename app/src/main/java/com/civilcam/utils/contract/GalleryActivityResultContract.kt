package com.civilcam.utils.contract

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

class GalleryActivityResultContract : ActivityResultContract<Unit, Uri>() {

    override fun createIntent(context: Context, input: Unit): Intent =
        if (Build.VERSION.SDK_INT >= 33) {
            Intent().apply {
                action = MediaStore.ACTION_PICK_IMAGES
                type = INTENT_TYPE_IMAGES
            }
        } else {
            Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = INTENT_TYPE_IMAGES
            }
        }


    override fun parseResult(resultCode: Int, intent: Intent?): Uri =
        (intent?.data ?: Uri.parse(""))

    companion object {
        private const val INTENT_TYPE_IMAGES = "image/*"
    }
}