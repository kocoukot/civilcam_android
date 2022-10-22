package com.civilcam.ui.auth.contract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import timber.log.Timber

class GoogleAuthResultContract : ActivityResultContract<Intent, String?>() {

    override fun createIntent(context: Context, input: Intent): Intent = input

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        val token = intent?.let {
            GoogleSignIn.getSignedInAccountFromIntent(intent)
                .addOnFailureListener {
                    Timber.i("login exception ${it.message}")
                }
        }.takeIf {
            it?.isSuccessful ?: false
        }?.result?.serverAuthCode
        return token
    }

}