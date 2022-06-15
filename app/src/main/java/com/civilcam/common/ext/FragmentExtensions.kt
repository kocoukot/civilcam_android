package com.civilcam.common.ext

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.civilcam.CivilcamApplication.Companion.instance
import com.civilcam.ui.common.alert.AlertTypes
import com.civilcam.ui.common.alert.DialogAlertFragment
import com.standartmedia.ui.common.loading.DialogLoadingFragment


fun Fragment.showToast(text: String = "") {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}


fun Fragment.showLoadingFragment(isShow: Boolean) {
    try {
        DialogLoadingFragment.create(childFragmentManager, isShow)
    } catch (e: Exception) {
        println(e.localizedMessage)
    }
}

fun Fragment.showAlertDialogFragment(
    title: String = "Something went wrong",
    text: String = "",
    alertType: AlertTypes = AlertTypes.OK,
    onOptionSelected: ((Boolean) -> Unit)? = null
) {
    DialogAlertFragment.create(
        childFragmentManager, title,
        text,
        alertType,
        onOptionSelected
    )
}

fun getTextHintColor(needWhite: Boolean): ColorStateList {
    val states = arrayOf(
        intArrayOf(-android.R.attr.state_checked),
        intArrayOf(android.R.attr.state_checked),
        intArrayOf(android.R.attr.state_pressed),
    )

    val colorsWhite = intArrayOf(
        Color.parseColor("#FFFFFFFF"),
        Color.parseColor("#FFFFFFFF"),
        Color.parseColor("#FFFFFFFF"),
    )

    val colorsGrey = intArrayOf(
        Color.parseColor("#7C7D7E"),
        Color.parseColor("#7C7D7E"),
        Color.parseColor("#7C7D7E"),
    )

    return ColorStateList(states, if (needWhite) colorsWhite else colorsGrey)
}

fun Fragment.getColorFromRes(color: Int) =
    ContextCompat.getColor(requireContext(), color)

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

private fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

private fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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
