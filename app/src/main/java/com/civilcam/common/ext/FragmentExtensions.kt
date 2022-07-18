package com.civilcam.common.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.civilcam.CivilcamApplication.Companion.instance
import com.civilcam.R
import com.civilcam.ui.common.alert.AlertDialogTypes
import com.civilcam.ui.common.alert.DialogAlertFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.standartmedia.ui.common.loading.DialogLoadingFragment

fun Fragment.showDatePicker(
    updatedDate: (Long) -> Unit
) {

    val picker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select date")
//        .setCalendarConstraints(DateUtils.getRestrictsForPast())
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()

    picker.show(parentFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener {
        updatedDate(it)
    }
}


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

fun Fragment.setPan() {
    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
}

fun Fragment.setResize() {
    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
}


fun Fragment.hideSystemUI() {
    activity?.window?.apply {
        statusBarColor = Color.TRANSPARENT
//            if (Build.VERSION.SDK_INT in 21..29) {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        WindowCompat.setDecorFitsSystemWindows(this, false)
    }
}

fun Fragment.showSystemUI() {

    activity?.window?.apply {
//            if (Build.VERSION.SDK_INT in 21..29) {
        statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            } else if (Build.VERSION.SDK_INT >= 30) {
        WindowCompat.setDecorFitsSystemWindows(this, true)
//            }
    }
}
