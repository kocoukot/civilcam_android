package com.civilcam.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.civilcam.common.ext.navigateByDirection
import com.civilcam.databinding.ActivityMainBinding
import com.civilcam.domainLayer.castSafe
import com.civilcam.ext_features.SupportBottomBar
import com.civilcam.service.notifications.NotificationRequestButtonListener.Companion.ARG_MAP_ALERT_ID
import com.civilcam.ui.common.NavigationDirection
import com.civilcam.ui.common.ext.setupWithNavController
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navHost: NavHostFragment? = null

    private val currentVisibleFragment: Fragment?
        get() = navHost?.childFragmentManager?.fragments?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ActivityMainBinding.inflate(layoutInflater)
                .also { binding = it }
                .root
        )

        navHost = supportFragmentManager.findFragmentById(binding.navHostView.id)
            .castSafe<NavHostFragment>()

        navHost?.apply {
            childFragmentManager.addOnBackStackChangedListener(onBackStackChangedListener)
        }

        navHost?.let { nav ->
            binding.bottomNavigationView.setupWithNavController(
                navController = nav.navController
            ) {}
        }

        intent?.extras?.getParcelable<NavigationDirection>(EXTRA_DIRECTION)
            ?.let(::navigateByDirection)
    }

    private fun navigateByDirection(direction: NavigationDirection) {
        navHost?.navController?.navigateByDirection(direction)
    }

    private val onBackStackChangedListener by lazy {
        FragmentManager.OnBackStackChangedListener {
            binding.navBarGroup.isVisible = currentVisibleFragment is SupportBottomBar
        }
    }

    fun showBottomNavBar(isVisible: Boolean) {
        binding.navBarGroup.isVisible = isVisible
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.let { extras ->
            val userId = extras.getInt(ARG_MAP_ALERT_ID)
            Timber.i("onNewIntent user id $userId")
//            navHost?.navController?.navigate(R.id.liveMapFragment, LiveMapFragment.createArgs(userId))
        }
    }


    companion object {
        const val FROM_NOTIFICATION = "fromNotification"
        const val EXTRA_DIRECTION = "extra_nav_direction"
        const val SHARED_RATING = "sharedRating"
    }
}