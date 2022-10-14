package com.civilcam.ui.emergency.content

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.civilcam.R
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.ext_features.compose.elements.LocationData
import com.civilcam.ext_features.compose.elements.LocationDetectButton
import com.civilcam.ext_features.ext.loadIcon
import com.civilcam.ui.emergency.model.EmergencyActions
import com.civilcam.ui.emergency.model.EmergencyUserModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch


@Composable
fun EmergencyMapContent(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    screenState: EmergencyScreen,
    isLocationAllowed: Boolean,
    userLocationData: EmergencyUserModel?,
    onActionClicked: (EmergencyActions) -> Unit
) {

    val scope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(45.0, -98.0), 0f)
    }

    LaunchedEffect(key1 = userLocationData != null) {
        userLocationData?.let { loc ->
            scope.launch {
                cameraPositionState
                    .animate(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.fromLatLngZoom(
                                loc.userLocation,
                                15f
                            )
                        ), 500
                    )
            }
        }
    }

    var userLocationAddress by remember {
        mutableStateOf(userLocationData?.locationData)
    }
    userLocationAddress = userLocationData?.locationData
    Box(modifier = modifier.fillMaxSize()) {
        var tobBarModifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)

        if (screenState == EmergencyScreen.NORMAL)
            tobBarModifier = tobBarModifier.statusBarsPadding()

        GoogleMap(
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false, compassEnabled = false)
        ) {
            userLocationData?.let { userLocation ->
                Marker(
                    state = MarkerState(position = userLocation.userLocation),
                    rotation = userLocation.userBearing,
                    icon = bitmapUserAvatarFromVector(LocalContext.current),
                )
            }

            userLocationData?.guardsLocation?.takeIf { it.isNotEmpty() }?.let { guardsList ->
                val guards by remember {
                    derivedStateOf {
                        guardsList
                    }
                }
                for (user in guards) {
                    user.avatar.imageUrl?.let { avatarUrl ->
                        val bitmap = loadIcon(
                            LocalContext.current,
                            avatarUrl,
                            R.drawable.img_avatar
                        )

                        Marker(
                            state = MarkerState(position = LatLng(user.latitude, user.longitude)),
                            icon = bitmap,
                        )
                    }
                }
            }
        }

        EmergencyTopBarContent(
            modifier = tobBarModifier,
            avatarUrl = avatarUrl,
            locationDetectContent = {
                LocationDetectButton(
                    isAllowed = isLocationAllowed,
                    onDetectLocation = {
                        onActionClicked.invoke(EmergencyActions.DetectLocation)
                        userLocationData?.userLocation?.let { location ->
                            scope.launch {
                                cameraPositionState
                                    .animate(
                                        CameraUpdateFactory.newCameraPosition(
                                            CameraPosition.fromLatLngZoom(
                                                location,
                                                15f
                                            )
                                        ), 500
                                    )
                            }
                        }
                    },
                )
            },
            locationDataContent = {
                userLocationAddress?.let { LocationData(it) }
            },
            screen = screenState,
            onClick = { action ->
                onActionClicked.invoke(action)
            }
        )
    }
}

private fun bitmapUserAvatarFromVector(
    context: Context,
): BitmapDescriptor? {

    val drawable = ContextCompat.getDrawable(context, R.drawable.ic_user_location) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}

//fun loadIcon(
//    context: Context,
//    url: String?,
//    placeHolder: Int,
//): BitmapDescriptor? {
//    try {
//        var bitmap: Bitmap? = null
//        Glide.with(context)
//            .asBitmap()
//            .load(url)
//            .error(placeHolder)
//            // to show a default icon in case of any errors
//            .into(object : CustomTarget<Bitmap>() {
//                override fun onResourceReady(
//                    resource: Bitmap,
//                    transition: Transition<in Bitmap>?
//                ) {
//
//                    bitmap = getRoundedCornerBitmap(resource)
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//
//                }
//            })
//        return BitmapDescriptorFactory.fromBitmap(bitmap!!)
//    } catch (e: Exception) {
//        e.printStackTrace()
//        return null
//    }
//
//}
//
//fun getRoundedCornerBitmap(bitmap: Bitmap): Bitmap? {
//    val bitMapSize = 40.toDp()
//    val odds = 2.toDp()
//
//    val backgroundCircle = Bitmap.createBitmap(
//        bitMapSize,
//        bitMapSize, Bitmap.Config.ARGB_8888
//    )
//    backgroundCircle.eraseColor(Color.RED)
//
//    val outputBitmap = Bitmap.createBitmap(
//        backgroundCircle.width,
//        backgroundCircle.height,
//        Bitmap.Config.ARGB_8888
//    )
//    val resizedAvatar = getRoundedBitmap(Bitmap.createScaledBitmap(bitmap, bitMapSize, bitMapSize, true))
//    val resizedBackground =
//        getRoundedBitmap(Bitmap.createScaledBitmap(backgroundCircle, bitMapSize, bitMapSize, true))
//
//    val outputCanvas = Canvas(outputBitmap);
//
//    val paint = Paint().apply {
//        color = Color.RED
//        isAntiAlias = true
//    }
//
//
//    val outerRect = Rect(0, 0, resizedBackground.height, resizedBackground.width)
//    val innerRect = Rect(odds, odds, resizedBackground.height - odds, resizedBackground.width - odds)
//
//    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.ADD)
//
//
//    outputCanvas.drawBitmap(resizedBackground, outerRect, outerRect, paint)
//    outputCanvas.drawBitmap(resizedAvatar, outerRect, innerRect, null)
//    return outputBitmap
//}
//
//fun getRoundedBitmap(bitmap: Bitmap): Bitmap {
//    val resultBitmap: Bitmap
//    val originalWidth = bitmap.width
//    val originalHeight = bitmap.height
//    val r: Float = (originalHeight).toFloat()
//    resultBitmap = Bitmap.createBitmap(
//        originalHeight, originalHeight,
//        Bitmap.Config.ARGB_8888
//    )
//
//    val canvas = Canvas(resultBitmap)
//    val paint = Paint().apply {
//    }
//    val rect = Rect(
//        0,
//        0, originalWidth, originalHeight
//    )
//    paint.isAntiAlias = true
//
//    val rect1 = Rect(0, 0, resultBitmap.height, resultBitmap.width)
//
//    val rectF = RectF(rect1)
//
//
//    canvas.drawRoundRect(rectF, r, r, paint)
//    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
//    canvas.drawBitmap(bitmap, rect, rect, paint)
//    return resultBitmap
//}