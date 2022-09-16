package com.civilcam.ui

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.civilcam.common.ext.navigateByDirection
import com.civilcam.databinding.ActivityMainBinding
import com.civilcam.domainLayer.castSafe
import com.civilcam.ext_features.SupportBottomBar
import com.civilcam.ext_features.setupWithNavController
import com.civilcam.service.CCFireBaseMessagingService
import com.civilcam.ui.common.NavigationDirection
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navHost: NavHostFragment? = null

    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

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
        getNewIntentLogic(intent = intent)
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
        this.intent = intent
        getNewIntentLogic(intent)
    }

    private fun getNewIntentLogic(intent: Intent?) {
        // Timber.d("message received new logic intent ${intent}")
        intent?.extras?.let { extras ->
            extras.getInt(ARG_MAP_ALERT_ID).takeIf { it > 0 }?.let { alertId ->
                Timber.tag("alert notif ID").i("main activity userId $alertId")

                CCFireBaseMessagingService.cancelAlertProgress()
                (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).cancel(
                    CCFireBaseMessagingService.NOTIFICATION_ALERT_ID
                )
                Handler(Looper.getMainLooper()).postDelayed({
                    navHost?.navController?.navigate("civilcam://liveMapFragment/$alertId".toUri())
                }, 500)
            }
        }
    }

    companion object {
        const val ARG_MAP_ALERT_ID = "map_alert_id"
        const val FROM_NOTIFICATION = "fromNotification"
        const val EXTRA_DIRECTION = "extra_nav_direction"
        const val SHARED_RATING = "sharedRating"
    }
}