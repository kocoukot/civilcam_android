package com.civilcam.service


//class CCFireBaseMessagingService : FirebaseMessagingService() {
//
//    private val setFcmTokenUseCase: SetFcmTokenUseCase by inject()
//
//    private val notificationManager by lazy {
//        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//
//        Timber.d("Init Firebase Service")
//
//    }
//
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        Timber.d("create a new FCM Token $token")
//        // Need to safe token in preference to take it when make log in or sinUP
//        setFcmTokenUseCase(token)
//    }
//
//    // To show Notification in Foreground
//    @SuppressLint("UnspecifiedImmutableFlag", "ResourceAsColor")
//    override fun onMessageReceived(p0: RemoteMessage) {
//        super.onMessageReceived(p0)
//
//        Timber.d("Message Recived " + p0.notification?.body)
//
//        val intent = Intent(this, MainActivity::class.java).apply {
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            putExtra(
//                MainActivity.FROM_NOTIFICATION,
//                true
//            )
//            addCategory(Intent.CATEGORY_LAUNCHER)
//        }
//
//        val pendingIntent = PendingIntent
//            .getActivity(
//                this,
//                0,
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//
//
//        val channelId = getString(R.string.digi_app_name)
//        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.img_digi_face)
//            .setContentTitle(p0.notification?.title)
//            .setContentText(p0.notification?.body)
//            .setNumber(notificationManager.activeNotifications.count())
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
//            .setStyle(NotificationCompat.BigTextStyle())
//
//        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        val channel = NotificationChannel(
//            channelId,
//            "Default channel",
//            NotificationManager.IMPORTANCE_DEFAULT
//        )
//        channel.setShowBadge(true)
//        manager.createNotificationChannel(channel)
//
//        val notification = builder.build()
//        manager.notify(0, notification)
//    }
//}