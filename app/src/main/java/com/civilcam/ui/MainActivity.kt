package com.civilcam.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.civilcam.R
import com.civilcam.databinding.ActivityMainBinding
import com.civilcam.service.notifications.NotificationHelper
import com.civilcam.ui.common.SupportBottomBar
import com.civilcam.ui.common.ext.setupWithNavController
import com.standartmedia.common.ext.castSafe


class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private var navHost: NavHostFragment? = null

	private val currentVisibleFragment: Fragment?
		get() = navHost?.childFragmentManager?.fragments?.first()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		window.apply {
			navigationBarColor = ContextCompat.getColor(context, R.color.black)
		}
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
	}

	override fun onResume() {
		super.onResume()
		binding.showNotification.apply {
			setOnClickListener {
//				val mManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//				val notification = Notification(R.drawable.ic_avatar_placeholder, title, System.currentTimeMillis())
//				val contentView = RemoteViews(context.packageName, R.layout.view_notification_big)
//				contentView.setProgressBar(R.id.progressBar, 10, 0, false)
//				contentView.setTextViewText(R.id.notification_title, text)
//				notification.contentView = contentView
//				notification.contentView.setProgressBar(R.id.progressBar, 10, 5, false);
//
//				val notificationIntent = Intent(context, MainActivity::class.java)
//				val contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)
//				notification.contentIntent = contentIntent
//				mManager.notify(0, notification)
//

				NotificationHelper().showRequestNotification(this@MainActivity)
			}

			setOnLongClickListener {
				true
			}
		}
	}
}