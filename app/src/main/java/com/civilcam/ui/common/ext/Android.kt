package com.civilcam.ui.common.ext

import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.Keep
import androidx.annotation.MainThread
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.viewbinding.ViewBinding
import com.civilcam.domainLayer.cast
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.toDp
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.ref.WeakReference
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@MainThread
fun <T : ViewBinding> Fragment.viewBinding(
    binder: (view: View) -> T
) = ViewBindingDelegate(binder) { viewLifecycleOwner }

class ViewBindingDelegate<T : ViewBinding>(
    private val binder: (view: View) -> T,
    private val viewLifecycleOwnerProvider: () -> LifecycleOwner
) : ReadOnlyProperty<Fragment, T> {
    private val lifecycleObserver: BindingLifecycleObserver = BindingLifecycleObserver()
    private val viewLifecycleOwner: LifecycleOwner
        get() = viewLifecycleOwnerProvider.invoke()
    private var isViewCreated = false
    private var binding: T? = null

    override operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        return if (!isViewCreated || binding == null) {
            binder.invoke(thisRef.requireView())
                .also { binding = it }
        } else {
            binding!!
        }
    }

    private inner class BindingLifecycleObserver : LifecycleObserver {
        private val handler = Handler(Looper.getMainLooper())

        @Keep
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreateView() {
            isViewCreated = true
        }

        @Keep
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroyView() {
            if (isViewCreated) {
                isViewCreated = false
                viewLifecycleOwner.lifecycle.removeObserver(this)
                handler.post {
                    binding = null
                }
            }
        }
    }
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

fun Fragment.applyStatusBarColor(@ColorRes color: Int) = activity?.apply {
    window.statusBarColor = ContextCompat.getColor(this, color)
}

inline fun <reified T> Fragment.arg(key: String): Lazy<T?> =
    lazy { this.arguments?.get(key) as? T? }

inline fun <reified T> Fragment.arg(key: String, defaultValue: T): Lazy<T> =
    lazy { this.arguments?.get(key) as? T? ?: defaultValue }

inline fun <reified T> Fragment.requireArg(key: String): Lazy<T> =
    lazy { this.requireArguments().get(key) as T }

fun <T> Fragment.observeResult(
    key: String,
    defaultValue: T? = null,
    observer: (T) -> Unit
) {
    val savedState = navController
        .currentBackStackEntry
        ?.savedStateHandle
    savedState
        ?.getLiveData(key, defaultValue)
        ?.observeNonNull(viewLifecycleOwner) {
            savedState.set(key, null)
            observer.invoke(it!!)
        }
}

fun <T> Fragment.setResult(key: String, value: T?) =
    navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.set(key, value)

fun <T> Fragment.getResult(
    key: String,
    defaultValue: T?
): T? {
    val savedState = navController
        .currentBackStackEntry
        ?.savedStateHandle
    val value = savedState?.get<T>(key)
    savedState?.set(key, null)
    return value
}

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

fun ConstraintLayout.updateConstraintSet(update: ConstraintSet.() -> Unit) = ConstraintSet()
    .also {
        it.clone(this)
        update.invoke(it)
        setConstraintSet(it)
    }

fun <T : Fragment> T.withArgs(args: Bundle) = this.apply { arguments = args }

val Fragment.navController
    get() = findNavController()

fun Rect.setBounds(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) =
    set(left, top, right, bottom)

var ViewBinding.isRootVisible: Boolean
    get() = root.isVisible
    set(value) {
        root.isVisible = value
    }

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams() = layoutParams
    .cast<T>()

fun BottomNavigationView.setupWithNavController(
    navController: NavController,
    onNavigationItemSelected: (MenuItem) -> Unit,

    ) {
    setOnNavigationItemSelectedListener {
        onNavigationItemSelected(it)
        onNavDestinationSelected(it, navController)
        true
    }
    val weakReference = WeakReference<BottomNavigationView>(this)
    navController.addOnDestinationChangedListener(
        object : OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination, arguments: Bundle?
            ) {
                val view = weakReference.get()
                if (view == null) {
                    navController.removeOnDestinationChangedListener(this)
                    return
                }
                val menu = view.menu
                var h = 0
                val size = menu.size()
                while (h < size) {
                    val item = menu.getItem(h)
                    if (matchDestination(destination, item.itemId)) {
                        item.isChecked = true
                    }
                    h++
                }
            }
        })
}

private fun matchDestination(
    destination: NavDestination,
    @IdRes destId: Int
): Boolean {
    var currentDestination = destination
    while (currentDestination.id != destId && currentDestination.parent != null) {
        currentDestination.parent?.let { currentDestination = it }
    }
    return currentDestination.id == destId
}

fun View.setMarginTop(marginTop: Int) {
    val systemWindow = this.layoutParams as ViewGroup.MarginLayoutParams
    systemWindow.setMargins(0, marginTop, 0, 0)
    this.layoutParams = systemWindow
}

fun View.setMarginBottom(marginBottom: Int) {
    val systemWindow = this.layoutParams as ViewGroup.MarginLayoutParams
    systemWindow.setMargins(0, 0, 0, marginBottom)
    this.layoutParams = systemWindow
}

fun View.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
    if (this.layoutParams is MarginLayoutParams) {
        val p = this.layoutParams as MarginLayoutParams
        p.setMargins(left, top, right.toDp(), bottom.toDp())
        this.requestLayout()
    }
}
