package com.civilcam.ui

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.civilcam.R
import com.civilcam.common.ext.navigateByDirection
import com.civilcam.common.ext.navigateToRoot
import com.civilcam.databinding.ActivityMainBinding
import com.civilcam.domainLayer.castSafe
import com.civilcam.domainLayer.model.subscription.UserSubscriptionState
import com.civilcam.domainLayer.model.user.LanguageType
import com.civilcam.domainLayer.usecase.subscriptions.SetExpiredSubscriptionUseCase
import com.civilcam.domainLayer.usecase.user.GetLocalCurrentUserUseCase
import com.civilcam.domainLayer.usecase.user.IsUserLoggedInUseCase
import com.civilcam.ext_features.SupportBottomBar
import com.civilcam.ext_features.arch.EndSubscription
import com.civilcam.ext_features.arch.VoiceRecord
import com.civilcam.ext_features.ext.LanguageCheck
import com.civilcam.ext_features.setupWithNavController
import com.civilcam.langselect.LanguageSelectFragment
import com.civilcam.service.CCFireBaseMessagingService
import com.civilcam.service.location.LocationService
import com.civilcam.service.location.LocationService.Companion.ACTION_START
import com.civilcam.service.location.LocationService.Companion.ACTION_STOP
import com.civilcam.service.voice.VoiceRecognition
import com.civilcam.socket_feature.SocketHandler
import com.civilcam.ui.common.NavigationDirection
import com.civilcam.ui.emergency.EmergencyFragment
import com.civilcam.ui.network.main.NetworkMainFragment
import com.civilcam.ui.network.main.model.NetworkScreen
import com.civilcam.ui.subscription.SubscriptionFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber


class MainActivity : AppCompatActivity(), VoiceRecord, EndSubscription, LanguageCheck {
	private val isUserLoggedInUseCase: IsUserLoggedInUseCase by inject()
	private val getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase by inject()
	private val setExpiredSubscriptionUseCase: SetExpiredSubscriptionUseCase by inject()

	private val recognizer = VoiceRecognition.Base(this) {
		navHost?.navController?.navigateToRoot(
			R.id.emergency_root, args = EmergencyFragment.createArgs(true)
		)
	}
	private lateinit var binding: ActivityMainBinding
	private var navHost: NavHostFragment? = null
	
	private val notificationManager by lazy {
		getSystemService(NOTIFICATION_SERVICE) as NotificationManager
	}
	
	private val currentVisibleFragment: Fragment?
		get() = navHost?.childFragmentManager?.fragments?.first()
	
	private val onBackStackChangedListener by lazy {
		FragmentManager.OnBackStackChangedListener {
			binding.navBarGroup.isVisible = currentVisibleFragment is SupportBottomBar
			if (currentVisibleFragment is LanguageSelectFragment) {
				stopLocationService()
				recognizer.destroy()
			}
			//checkSubscriptionState()
		}
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
		
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
			?.let(::navigateByDirection) ?: run {
			if (isUserLoggedInUseCase()) getLocalCurrentUserUseCase()?.let {
				NavigationDirection.resolveDirectionFor(
					it
				)
			}?.let {
				navigateByDirection(
					it
				)
			}
		}.also {
			getNewIntentLogic(intent = intent)
		}


//        AndroidLoggingHandler.reset(AndroidLoggingHandler())
//        Logger.getLogger(Socket::class.java.name).level = Level.ALL
//        Logger.getLogger(SocketHandler::class.java.name).level = Level.ALL
//        Logger.getLogger(Manager::class.java.name).level = Level.ALL
		
		if (isUserLoggedInUseCase()) {
			startLocationService()
			//recognizer.initRecorder()
		}
		Timber.tag("alert_notif_ID").i("main activity on create")
	}


	override fun startVoiceRecord() {
		recognizer.startRecord(this)
	}
	
	override fun stopVoiceRecord() {
		recognizer.stopRecord(this)
	}
	
	override fun onStop() {
		super.onStop()
		recognizer.destroy()
	}
	
	override fun onResume() {
		super.onResume()
		if (isUserLoggedInUseCase()) {
			//startVoiceRecord()
		}
	}
	
	fun startLocationService() {
		if (isUserLoggedInUseCase()) {
			Intent(applicationContext, LocationService::class.java).apply {
				action = ACTION_START
				startService(this)
			}
			
		}
	}
	
	private fun stopLocationService() {
		Intent(applicationContext, LocationService::class.java).apply {
			action = ACTION_STOP
			startService(this)
		}
		
	}
	
	override fun onDestroy() {
		stopLocationService()
		SocketHandler.closeConnection()
		recognizer.destroy()
		(this.getSystemService(Context.AUDIO_SERVICE) as AudioManager).adjustStreamVolume(
			AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0
		)

		super.onDestroy()
	}
	
	private fun navigateByDirection(direction: NavigationDirection) {
		navHost?.navController?.navigateByDirection(direction)
	}
	
	
	fun showBottomNavBar(isVisible: Boolean) {
		binding.navBarGroup.isVisible = isVisible
	}
	
	override fun onNewIntent(intent: Intent?) {
		super.onNewIntent(intent)
		this.intent = intent
		getNewIntentLogic(intent)
		Timber.tag("alert_notif_ID").i("main activity onNewIntent")
	}
	
	private fun getNewIntentLogic(intent: Intent?) {
		// Timber.d("message received new logic intent ${intent}")
		
		intent?.extras?.let { extras ->
			Timber.tag("alert_notif_ID")
				.i("main activity userId ${extras.getInt(CCFireBaseMessagingService.EXTRAS_NOTIFICATION_ALERT_ID)}")
			
			extras.getInt(CCFireBaseMessagingService.EXTRAS_NOTIFICATION_ALERT_ID).takeIf { it > 0 }
				?.let { alertId ->
					Timber.tag("alert_notif_ID").i("main activity userId $alertId")
					
					CCFireBaseMessagingService.cancelAlertProgress()
					(getSystemService(NOTIFICATION_SERVICE) as NotificationManager).cancel(
						CCFireBaseMessagingService.NOTIFICATION_ALERT_ID + alertId
					)
					Handler(Looper.getMainLooper()).postDelayed({
						navHost?.navController?.navigate("civilcam://liveMapFragment/$alertId".toUri())
					}, 500)
				}
			
			extras.getInt(CCFireBaseMessagingService.EXTRAS_NOTIFICATION_REQUEST_ID_KEY)
				.takeIf { it > 0 }?.let { requestId ->
					Timber.tag("alert_notif_ID").i("main activity userId $requestId")
					
					CCFireBaseMessagingService.cancelRequestProgress()
					(getSystemService(NOTIFICATION_SERVICE) as NotificationManager).cancel(
						CCFireBaseMessagingService.NOTIFICATION_REQUESTS_ID + requestId
					)
					Handler(Looper.getMainLooper()).postDelayed({
						navHost?.navController?.navigate(
							R.id.network_root,
							NetworkMainFragment.createArgs(NetworkScreen.REQUESTS)
						)
					}, 500)
				}
		}
	}
	
	
	override fun subscriptionEnd() {
		if (isUserLoggedInUseCase()) {
			setExpiredSubscriptionUseCase()
			binding.bottomNavigationView.isVisible = false
			navHost?.navController?.navigateToRoot(
				R.id.subscriptionFragment, args = SubscriptionFragment.createArgs(
					UserSubscriptionState.SUB_EXPIRED
				)
			)
			stopLocationService()
			lifecycleScope.launch(Dispatchers.Main) {
				delay(500)
				recognizer.destroy()
			}
		}
	}

	companion object {
		const val EXTRA_DIRECTION = "extra_nav_direction"
	}

	override fun checkLanguage() {
		binding.bottomNavigationView.menu.forEach {
			when (it.itemId) {
				R.id.network_root -> {
					it.title = getString(R.string.navigation_bar_network)
				}
				R.id.emergency_root -> {
					it.title = getString(R.string.navigation_bar_emergency)
				}
				R.id.alerts_graph -> {
					it.title = getString(com.civilcam.domainLayer.R.string.alerts_root_list_title)
				}
			}
		}
	}

	override fun setLanguage(language: LanguageType) {
//		this.getSystemService(LocaleManager::class.java)
//			.setApplicationLocales(LocaleList(Locale.forLanguageTag(language.langValue)))
	}
}