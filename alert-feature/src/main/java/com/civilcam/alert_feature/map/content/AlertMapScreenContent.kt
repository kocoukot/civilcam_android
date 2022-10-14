package com.civilcam.alert_feature.map.content

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.civilcam.alert_feature.R
import com.civilcam.alert_feature.map.model.LiveMapActions
import com.civilcam.alert_feature.map.model.OnGuardUserData
import com.civilcam.alert_feature.map.model.UserAlertLocationData
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.ext_features.compose.elements.IconActionButton
import com.civilcam.ext_features.compose.elements.LocationData
import com.civilcam.ext_features.compose.elements.LocationDetectButton
import com.civilcam.ext_features.ext.loadIcon
import com.civilcam.ext_features.ext.toDp
import com.civilcam.ext_features.theme.CCTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@Composable
fun AlertMapScreenContent(
    modifier: Modifier = Modifier,
    isLocationAllowed: Boolean,
    alertScreenState: EmergencyScreen,
    guardianInformation: OnGuardUserData? = null,
    userAlertLocationData: UserAlertLocationData? = null,
    onActionClick: (LiveMapActions) -> Unit
) {


    val scope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(45.0, -98.0), 0f)
    }

    LaunchedEffect(key1 = userAlertLocationData != null) {
        userAlertLocationData?.let { loc ->
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
        mutableStateOf(guardianInformation?.person?.address)
    }

    val topBarPadding by animateDpAsState(
        targetValue = if (alertScreenState == EmergencyScreen.COUPLED) 16.dp else 68.dp,
        animationSpec = tween(delayMillis = 250)
    )
    Box(modifier = modifier.background(CCTheme.colors.grayOne)) {

        GoogleMap(
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false, compassEnabled = false)
        ) {
            userAlertLocationData?.let {
                Marker(
                    state = MarkerState(position = userAlertLocationData.userLocation),
                    rotation = userAlertLocationData.userBearing,
                    icon = bitmapUserAvatarFromVector(LocalContext.current),
                )
            }

            guardianInformation?.let {
                val onGuardUser by remember { derivedStateOf { it } }
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            onGuardUser.person.latitude,
                            onGuardUser.person.longitude
                        )
                    ),
                    icon = loadIcon(
                        LocalContext.current,
                        onGuardUser.person.avatar.imageUrl,
                        R.drawable.img_avatar
                    ),
                )
            }

        }

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = topBarPadding)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            LocationDetectButton(
                isAllowed = isLocationAllowed,
                onDetectLocation = {
                    onActionClick.invoke(LiveMapActions.ClickDetectLocation)
                    userAlertLocationData?.userLocation?.let { location ->
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

            userLocationAddress?.let { address -> LocationData(address) }

            Crossfade(targetState = alertScreenState) { state ->
                IconActionButton(
                    buttonIcon = if (state == EmergencyScreen.COUPLED)
                        R.drawable.ic_map_extended
                    else
                        R.drawable.ic_map_minimize,
                    buttonClick = {
                        onActionClick.invoke(
                            if (state == EmergencyScreen.COUPLED)
                                LiveMapActions.ClickScreenChange(EmergencyScreen.MAP_EXTENDED)
                            else
                                LiveMapActions.ClickScreenChange(EmergencyScreen.COUPLED)
                        )
                    },
                    tint = CCTheme.colors.primaryRed,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(color = CCTheme.colors.white)
                        .size(28.dp)
                )

            }
        }
    }
}


private fun bitmapUserAvatarFromVector(
    context: Context,
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, R.drawable.ic_user_location) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}

private fun bitmapDescriptorFromVector(
    context: Context,
    drawableInt: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, drawableInt) ?: return null
    drawable.setBounds(0, 0, 32.toDp(), 32.toDp())

    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}