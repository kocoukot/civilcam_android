package com.civilcam.ui.auth

import android.content.Intent
import androidx.fragment.app.Fragment
import com.civilcam.BuildConfig
import com.civilcam.ui.auth.contract.GoogleAuthResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


abstract class FragmentAuthHandler(
    protected val onSuccess: (String) -> Unit,
    protected val onError: (Throwable) -> Unit
) {

    abstract fun auth(fragment: Fragment)

    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}

class GoogleFragmentAuthHandler(
    fragment: Fragment,
    onSuccess: (String) -> Unit,
    onError: (Throwable) -> Unit
) : FragmentAuthHandler(onSuccess, onError) {

    private val googleAuthActivityLauncher =
        fragment.registerForActivityResult(GoogleAuthResultContract()) { token ->
            token?.let(onSuccess) ?: onError.invoke(Throwable("Login token was not get"))
        }

    override fun auth(fragment: Fragment) {
        GoogleSignIn.getLastSignedInAccount(fragment.requireContext())
            ?.takeIf { it.idToken != null }
            ?.let { it.idToken?.let(onSuccess::invoke) }
            ?: kotlin.run {

                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestServerAuthCode(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                    .requestEmail()
                    .build()

                with(GoogleSignIn.getClient(fragment.requireContext(), gso)) {
                    googleAuthActivityLauncher.launch(signInIntent)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    fun singOut(fragment: Fragment) {
        GoogleSignIn.getLastSignedInAccount(fragment.requireContext())
            ?.takeIf { !it.idToken.isNullOrEmpty() }
            ?: kotlin.run {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestServerAuthCode(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                    .requestEmail()
                    .build()

                with(GoogleSignIn.getClient(fragment.requireContext(), gso)) {
                    signOut()
                        .addOnCompleteListener {
                            onSuccess("")
                        }
                }
            }
    }
}

//
//class FacebookFragmentAuthHandler(
//    onSuccess: (String) -> Unit,
//    onError: (Throwable) -> Unit
//) : FragmentAuthHandler(onSuccess, onError) {
//    private val facebookCallbackManager = CallbackManager.Factory.create()
//
//    override fun auth(fragment: Fragment) {
//        AccessToken.getCurrentAccessToken()
//            ?.takeIf { !it.isExpired }
//            ?.let { it.token.let(onSuccess::invoke) }
//            ?: run {
//                with(LoginManager.getInstance()) {
//                    registerCallback(
//                        facebookCallbackManager,
//                        object : FacebookCallback<LoginResult> {
//
//                            override fun onSuccess(result: LoginResult) {
//                                result.accessToken.token.let(onSuccess::invoke)
//                                unregisterCallback(facebookCallbackManager)
//                            }
//
//                            override fun onCancel() {}
//
//                            override fun onError(error: FacebookException) = onError.invoke(error)
//                        })
//                    logInWithReadPermissions(
//                        fragment,
//                        listOf(PERMISSION_PUBLIC_PROFILE, PERMISSION_EMAIL)
//                    )
//                }
//            }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        facebookCallbackManager.onActivityResult(
//            requestCode,
//            resultCode,
//            data
//        )
//    }

//    companion object {
//        private const val PERMISSION_PUBLIC_PROFILE = "public_profile"
//        private const val PERMISSION_EMAIL = "email"
//        private const val PERMISSION_USER_FRIENDS = "user_friends"
//    }
//}

