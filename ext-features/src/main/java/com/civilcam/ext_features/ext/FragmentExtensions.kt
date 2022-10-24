package com.civilcam.ext_features.ext

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.civilcam.ext_features.R


fun Fragment.showToast(text: String = "") {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.navigateToStart() {
    navController.navigate(
        getString(R.string.direction_startScreen).toUri(),
        NavOptions.Builder()
            .setPopUpTo(navController.backStack.first.destination.id, false)
            .build()
    )
}

fun Fragment.navigateTo(string: String) {
    findNavController().navigate(Uri.parse(string))
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

val Fragment.navController
    get() = findNavController()

fun Fragment.registerForPermissionsResult(
    vararg permissions: String,
    onGranted: (Boolean) -> Unit
): FragmentPermissionsDelegate =
    registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val grantedPermissions = permissions
            .takeWhile { perms[it] ?: false }
        onGranted.invoke(grantedPermissions.size == permissions.size)
    }
        .let {
            FragmentPermissionsDelegate(it, permissions.toList().toTypedArray(), this)
        }

class FragmentPermissionsDelegate(
    private val activityResultLauncher: ActivityResultLauncher<Array<String>>,
    private val permissions: Array<String>,
    private val fragment: Fragment
) {

    fun checkSelfPermissions(): Boolean {
        val grantedPermissions = permissions
            .takeWhile {
                ActivityCompat.checkSelfPermission(
                    fragment.requireContext(),
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
        return grantedPermissions.size == permissions.size
    }

    fun requestPermissions() = activityResultLauncher.launch(permissions)
}

fun Fragment.callPhone(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    startActivity(intent)
}