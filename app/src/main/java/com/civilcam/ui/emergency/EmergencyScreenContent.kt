package com.civilcam.ui.emergency

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.emergency.content.EmergencyButtonContent
import com.civilcam.ui.emergency.content.EmergencyTopBarContent
import com.civilcam.ui.emergency.model.EmergencyActions
import com.google.android.gms.maps.model.LatLng

@Composable
fun EmergencyScreenContent(viewModel: EmergencyViewModel) {
	
	val state = viewModel.state.collectAsState()
	val singapore = LatLng(1.35, 103.87)
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(singapore, 10f)
//    }
	
	Scaffold(
		backgroundColor = CCTheme.colors.grayThree,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			EmergencyTopBarContent(
				onAvatarClicked = { viewModel.setInputActions(EmergencyActions.GoUserProfile) },
				onSettingsClicked = { viewModel.setInputActions(EmergencyActions.GoSettings) },
				locationData = state.value.location
			)
		}
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
		) {
			Column {
//            GoogleMap(
//                modifier = Modifier.fillMaxSize(),
//                cameraPositionState = cameraPositionState
//            ) {
////            Marker(
////                state = MarkerState(position = singapore),
////                title = "Singapore",
////                snippet = "Marker in Singapore"
////            )
//            }
			}
			
			EmergencyButtonContent(
				emergencyButton = state.value.emergencyButton,
				modifier = Modifier
					.align(Alignment.BottomCenter)
					.size(150.dp)
					.offset(y = (-32).dp),
				oneClick = {
					viewModel.setInputActions(EmergencyActions.OneClickSafe)
				},
				doubleClick = {
					viewModel.setInputActions(EmergencyActions.DoubleClickSos)
				},
			)
		}
	}
	
}
 