package com.civilcam.ui.network.main.content

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.guard.GuardianModel

@Composable
fun GuardianSearchContent(
    data: List<GuardianModel>
) {

    val context = LocalContext.current
    if (data.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                stringResource(id = R.string.add_guardian_search_title),
                style = CCTheme.typography.common_medium_text_bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(id = R.string.add_guardian_search_text),
                style = CCTheme.typography.common_text_regular
            )
        }
    }
}
