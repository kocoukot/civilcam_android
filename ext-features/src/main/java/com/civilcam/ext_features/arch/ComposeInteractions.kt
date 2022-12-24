package com.civilcam.ext_features.arch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.civilcam.ext_features.ext.navigateToStart
import com.civilcam.ext_features.navController
import kotlinx.coroutines.flow.StateFlow


interface ComposeActions

interface ComposeRouteNavigation {
    fun handleNavigation(fragment: Fragment, block: (() -> Unit)? = null)

    class DeepLinkNavigate(private val destination: Int, private val arguments: String = "") :
        ComposeRouteNavigation {
        override fun handleNavigation(fragment: Fragment, block: (() -> Unit)?) {
            fragment.navController.navigate(Uri.parse("${fragment.getString(destination)}$arguments"))
        }
    }

    class GraphNavigate(private val destination: Int, private val bundle: Bundle? = null) :
        ComposeRouteNavigation {
        override fun handleNavigation(fragment: Fragment, block: (() -> Unit)?) {
            fragment.navController.navigate(destination, bundle)
        }
    }

    object PopNavigation : ComposeRouteNavigation {
        override fun handleNavigation(fragment: Fragment, block: (() -> Unit)?) {
            fragment.navController.popBackStack()
        }
    }

    object NavigateToStart : ComposeRouteNavigation {
        override fun handleNavigation(fragment: Fragment, block: (() -> Unit)?) {
            fragment.navigateToStart()
        }
    }

    class ComposeRouteCallNumber(private val phoneNumber: String) : ComposeRouteNavigation {
        override fun handleNavigation(fragment: Fragment, block: (() -> Unit)?) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${phoneNumber.replace(Regex("[()]- "), "")}")
            fragment.startActivity(intent)
        }
    }

    object ComposeRouteFinishApp : ComposeRouteNavigation {
        override fun handleNavigation(fragment: Fragment, block: (() -> Unit)?) {
            fragment.requireActivity().finish()
        }

    }

    object CustomRoute : ComposeRouteNavigation {
        override fun handleNavigation(fragment: Fragment, block: (() -> Unit)?) {
            block?.invoke()
        }
    }

}


interface StateCommunication<T> {
    val state: StateFlow<T>
    fun updateInfo(info: suspend T.() -> T)
}

