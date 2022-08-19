package com.civilcam.ui.emergency.content

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.civilcam.R
import com.civilcam.common.ext.toDp
import com.civilcam.ui.emergency.model.EmergencyActions
import com.civilcam.ui.emergency.model.EmergencyScreen
import com.civilcam.ui.emergency.model.EmergencyUserModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun EmergencyMapContent(
    modifier: Modifier = Modifier,
    screenState: EmergencyScreen,
    userLocationData: EmergencyUserModel,
    onActionClicked: (EmergencyActions) -> Unit
) {

    val scope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocationData.userLocation, 13f)
    }
    val userLocationAddress = remember {
        derivedStateOf { userLocationData.locationData }
    }

    Box(modifier = modifier.fillMaxSize()) {
        var tobBarModifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)

        if (screenState == EmergencyScreen.NORMAL)
            tobBarModifier = tobBarModifier.statusBarsPadding()
        Timber.i("fetchUserLocationUseCase latlng ${userLocationData.userLocation} bearing ${userLocationData.userBearing}")

        GoogleMap(
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false, compassEnabled = false)
        ) {

            Marker(
                state = MarkerState(position = userLocationData.userLocation),
                rotation = userLocationData.userBearing,
                icon = bitmapUserAvatarFromVector(LocalContext.current),
            )

            for (user in userLocationData.guardsLocation) {
                Marker(
                    state = MarkerState(position = user),
                    icon = bitmapDescriptorFromVector(LocalContext.current, R.drawable.img_guard),
                )
            }
        }

        EmergencyTopBarContent(
            modifier = tobBarModifier,
            locationData = userLocationAddress.value,
            screen = screenState,
            onClick = { action ->
                if (action == EmergencyActions.DetectLocation) {
                    scope.launch {
                        cameraPositionState
                            .animate(
                                CameraUpdateFactory.newCameraPosition(
                                    CameraPosition.fromLatLngZoom(
                                        userLocationData.userLocation,
                                        15f
                                    )
                                ), 500
                            )
                    }
                }
                onActionClicked.invoke(action)
            }
        )
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


/*
https://stackoverflow.com/a/48356646
private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes  int vectorDrawableResourceId) {
    Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_map_pin_filled_blue_48dp);
    background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
    Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
    vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
    Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    background.draw(canvas);
    vectorDrawable.draw(canvas);
    return BitmapDescriptorFactory.fromBitmap(bitmap);
}*/