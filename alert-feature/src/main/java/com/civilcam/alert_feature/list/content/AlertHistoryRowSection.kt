package com.civilcam.alert_feature.list.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.alert_feature.R
import com.civilcam.ext_features.compose.elements.RowDivider
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun AlertHistoryRowSection(rowClicked: () -> Unit) {
    Column {


        Spacer(modifier = Modifier.height(32.dp))
        RowDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CCTheme.colors.white)
                .clickable {
                    rowClicked.invoke()
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                stringResource(id = R.string.alerts_history_title),
                style = CCTheme.typography.common_medium_text_regular,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(vertical = 12.dp)
                    .weight(1f)
            )
            Icon(
                painter = painterResource(id = com.civilcam.ext_features.R.drawable.ic_nav_forward),
                contentDescription = null,
                tint = CCTheme.colors.grayOne
            )

            Spacer(modifier = Modifier.padding(end = 8.dp))
        }
        RowDivider()
    }
}