package com.civilcam.ext_features

import android.graphics.Color
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment


fun Fragment.showToast(text: String = "") {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}


//fun Fragment.showLoadingFragment(isShow: Boolean) {
//    try {
//        DialogLoadingFragment.create(childFragmentManager, isShow)
//    } catch (e: Exception) {
//        println(e.localizedMessage)
//    }
//}
//
//fun Fragment.showAlertDialogFragment(
//    title: String = "Something went wrong",
//    text: String = "",
//    alertType: AlertDialogTypes = AlertDialogTypes.OK,
//    onOptionSelected: ((Boolean) -> Unit)? = null
//) {
//    DialogAlertFragment.create(
//        childFragmentManager, title,
//        text,
//        alertType,
//        onOptionSelected
//    )
//}

fun Fragment.setPan() {
    requireActivity().window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
}

fun Fragment.setResize() {
    requireActivity().window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
}


fun Fragment.hideSystemUI() {
    requireActivity().window?.apply {
        statusBarColor = Color.TRANSPARENT
//            if (Build.VERSION.SDK_INT in 21..29) {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        WindowCompat.setDecorFitsSystemWindows(this, false)
    }
}

fun Fragment.showSystemUI() {

    requireActivity().window?.apply {
//            if (Build.VERSION.SDK_INT in 21..29) {
        statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            } else if (Build.VERSION.SDK_INT >= 30) {
        WindowCompat.setDecorFitsSystemWindows(this, true)
//            }
    }
}
