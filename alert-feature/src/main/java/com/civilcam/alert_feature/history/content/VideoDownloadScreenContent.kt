package com.civilcam.alert_feature.history.content

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.civilcam.alert_feature.history.model.AlertHistoryActions
import com.civilcam.domainLayer.model.alerts.AlertDetailModel
import com.civilcam.domainLayer.model.alerts.VideoLoadingState
import com.civilcam.ext_features.compose.elements.RowDivider
import com.civilcam.ext_features.compose.elements.RowDividerGrayThree
import com.civilcam.ext_features.theme.CCTheme
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber

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
                DownLoadRow(item, onScreenAction::invoke)
                if (index < downloadsList.lastIndex) RowDividerGrayThree()
            }
        }
        RowDivider()
    }
}

@Composable
private fun DownLoadRow(
    rowInfo: AlertDetailModel.DownloadInfo,
    onRowAction: (AlertHistoryActions) -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    val videoData by remember { mutableStateOf(rowInfo) }
    var fileState by remember {
        mutableStateOf(rowInfo.videoState)
    }
    LaunchedEffect(key1 = Unit) {
        val work = WorkManager.getInstance(context)
        work.getWorkInfosForUniqueWorkLiveData("one_time_work" + rowInfo.id)
            .observe(lifecycle) {
                Timber.tag("video").d("workId ${it.first().state}")

                if (it.isNotEmpty()) {
                    when (it.first().state) {
                        WorkInfo.State.RUNNING -> {
                            fileState = VideoLoadingState.Loading
                        }
                        WorkInfo.State.SUCCEEDED -> {
                            if (fileState == VideoLoadingState.Loading) fileState =
                                VideoLoadingState.DownLoaded
                        }
                        WorkInfo.State.FAILED -> {
                            Timber.tag("video").d("workId FAILED ${it.first()}")
                            fileState = VideoLoadingState.ReadyToLoad
                            Toast.makeText(
                                context,
                                it.first().outputData.getString("key_loading_error"),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {}
                    }
                }
            }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onRowAction.invoke(AlertHistoryActions.ClickOpenVideo(rowInfo))
            },
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

        fileState.StateIcon(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(24.dp)
                .clip(CircleShape)
        ) {
            onRowAction.invoke(AlertHistoryActions.ClickDownloadVideo(rowInfo))
        }
    }
}

@Composable
fun CheckState(
    context: Context,
    workId: String,
    scope: CoroutineScope
) {

}
