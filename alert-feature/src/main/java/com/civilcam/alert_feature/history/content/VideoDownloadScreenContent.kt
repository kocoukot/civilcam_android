package com.civilcam.alert_feature.history.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.civilcam.alert_feature.history.model.AlertHistoryActions
import com.civilcam.domainLayer.model.alerts.AlertDetailModel
import com.civilcam.domainLayer.model.alerts.VideoLoadingState
import com.civilcam.ext_features.compose.elements.RowDivider
import com.civilcam.ext_features.compose.elements.RowDividerGrayThree
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun VideoDownloadScreenContent(
    downloadsList: List<AlertDetailModel.DownloadInfo>,
    onScreenAction: (AlertHistoryActions) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CCTheme.colors.white),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RowDivider()

        LazyColumn(modifier = Modifier) {
            itemsIndexed(downloadsList, key = { _, item -> item.id }) { index, item ->
                DownLoadRow(item)
                if (index < downloadsList.lastIndex) RowDividerGrayThree()
            }
        }
        RowDivider()
    }
}

@Composable
private fun DownLoadRow(rowInfo: AlertDetailModel.DownloadInfo) {
    var videoData by remember {
        mutableStateOf(rowInfo)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .padding(vertical = 14.dp),
            text = videoData.fileName,
            style = CCTheme.typography.common_text_medium,
        )

        Text(
            text = videoData.duration.toString(),
            style = CCTheme.typography.common_text_regular,
            color = CCTheme.colors.grayOne
        )

        videoData.videoState.StateIcon(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(24.dp)
                .clip(CircleShape)
        ) {
            videoData = videoData.copy(videoState = VideoLoadingState.Loading)
        }
    }
}