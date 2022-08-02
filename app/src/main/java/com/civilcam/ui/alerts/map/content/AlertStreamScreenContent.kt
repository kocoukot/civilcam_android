package com.civilcam.ui.alerts.map.content

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.alerts.map.model.LiveMapActions
import com.civilcam.ui.common.compose.IconActionButton
import com.civilcam.ui.emergency.content.EmergencyCameraPreview
import com.civilcam.ui.emergency.content.LiveAnimation
import com.civilcam.ui.emergency.model.EmergencyScreen
import com.civilcam.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AlertStreamScreenContent(
    modifier: Modifier = Modifier,
    alertScreenState: EmergencyScreen,
    cameraState: Int,
    onActionClick: (LiveMapActions) -> Unit
) {

    val bottomBarPadding by animateDpAsState(
        targetValue = if (alertScreenState == EmergencyScreen.COUPLED) 14.dp else 80.dp,
        animationSpec = tween(delayMillis = 250)
    )


    Box(modifier = modifier.background(CCTheme.colors.cianColor)) {
        EmergencyCameraPreview(
            cameraState = cameraState,
            modifier = Modifier.fillMaxSize()
        )
        LiveVideoBottomBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = bottomBarPadding)
                .align(Alignment.BottomCenter),
            alertScreenState = alertScreenState,
            onActionClick = onActionClick
        )

    }
}

@Composable
private fun LiveVideoBottomBar(
    modifier: Modifier = Modifier,
    alertScreenState: EmergencyScreen,
    onActionClick: (LiveMapActions) -> Unit
) {
    var currentLongTime by remember { mutableStateOf(System.currentTimeMillis()) }
    val scope = CoroutineScope(Dispatchers.Main)

    scope.launch {
        currentLongTime = System.currentTimeMillis()
        while (alertScreenState == EmergencyScreen.LIVE_EXTENDED || alertScreenState == EmergencyScreen.COUPLED) {
            delay(1000)
            currentLongTime = System.currentTimeMillis()
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LiveAnimation()

        Text(
            text = DateUtils.getFullDateAndTimeString(currentLongTime),
            style = CCTheme.typography.common_medium_text_regular,
            color = CCTheme.colors.white,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 16.dp, end = 2.dp)
        )

        Crossfade(targetState = alertScreenState) { targetState ->
            IconActionButton(
                buttonIcon = if (targetState == EmergencyScreen.COUPLED) R.drawable.ic_live_extend else R.drawable.ic_live_minimize,
                buttonClick = {
                    onActionClick.invoke(
                        if (targetState == EmergencyScreen.COUPLED)
                            LiveMapActions.ClickScreenChange(EmergencyScreen.LIVE_EXTENDED)
                        else
                            LiveMapActions.ClickScreenChange(EmergencyScreen.COUPLED)
                    )
                },
                tint = CCTheme.colors.white,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
            )
        }

    }
}