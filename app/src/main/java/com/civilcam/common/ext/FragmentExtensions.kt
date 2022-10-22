package com.civilcam.common.ext

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.fragment.app.Fragment
import com.civilcam.CivilcamApplication.Companion.instance
import com.civilcam.ext_features.alert.AlertDialogTypes
import com.civilcam.ui.common.alert.DialogAlertFragment


fun Fragment.showAlertDialogFragment(
    title: String = "Something went wrong",
    text: String = "",
    alertType: AlertDialogTypes = AlertDialogTypes.OK,
    onOptionSelected: ((Boolean) -> Unit)? = null
) {
    DialogAlertFragment.create(
        childFragmentManager, title,
        text,
        alertType,
        onOptionSelected
    )
}

fun isMobileOnline(): Boolean {
    val connectivityManager =
        instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                return true
            }
        }
    }
    return false
}