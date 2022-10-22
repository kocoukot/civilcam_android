package com.civilcam.service.location

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.civilcam.R
import com.civilcam.data.repository.LocationRepositoryImpl
import com.civilcam.domainLayer.repos.LocationRepository
import com.civilcam.domainLayer.usecase.user.SetUserCoordsUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class LocationService : Service() {

    private val setUserCoordsUseCase: SetUserCoordsUseCase by inject()
    private var isFetching = false
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationRepository

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationRepositoryImpl(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        if (!isFetching) {
            val notification = NotificationCompat.Builder(this, LOCATION_CHANNEL_ID)
                .setContentTitle("Tracking location")
                .setContentText("Location tracking for your safe")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            serviceScope.launch {
                isFetching = true
                try {
                    locationClient.fetchLocation(30000L)
                        .catch { e -> e.printStackTrace() }
                        .onEach { location ->
                            val lat = String.format("%.3f", location.first.latitude)
                            val lon = String.format("%.3f", location.first.longitude)
                            val updateNotification = notification.setContentText(
                                "Location: ($lat, $lon)"
                            )
                            kotlin.runCatching {
                                setUserCoordsUseCase(location.first)
                            }
                            notificationManager.notify(1, updateNotification.build())
                        }
                        .launchIn(serviceScope)
                } catch (e: Exception) {
                    Toast.makeText(this@LocationService, e.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            startForeground(1, notification.build())
        }
    }

    private fun stop() {
        isFetching = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val LOCATION_CHANNEL_ID = "location"

    }
}