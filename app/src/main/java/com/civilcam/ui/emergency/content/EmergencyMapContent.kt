package com.civilcam.ui.emergency.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.civilcam.ui.emergency.model.EmergencyActions
import com.civilcam.ui.emergency.model.EmergencyScreen

@Composable
fun EmergencyMapContent(
    modifier: Modifier = Modifier,
    screenState: EmergencyScreen,
    locationData: String,
    onActionClicked: (EmergencyActions) -> Unit
) {


    Box(modifier = modifier) {
        var tobBarModifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)

        if (screenState == EmergencyScreen.NORMAL)
            tobBarModifier = tobBarModifier.statusBarsPadding()

//        GoogleMap(
//            modifier = Modifier.fillMaxSize(),
//            cameraPositionState = cameraPositionState
//        ) {
//            Marker(
//                state = MarkerState(position = singapore),
//                title = "Singapore",
//                snippet = "Marker in Singapore"
//            )
//        }

        EmergencyTopBarContent(
            modifier = tobBarModifier,
            locationData = locationData,
            screen = screenState,
            onClick = { action ->
                onActionClicked.invoke(action)
            }
        )
    }

}