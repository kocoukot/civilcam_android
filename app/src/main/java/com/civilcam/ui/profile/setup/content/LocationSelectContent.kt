package com.civilcam.ui.profile.setup.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.civilcam.domainLayer.model.SearchModel
import com.civilcam.ext_features.compose.elements.RowDivider
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.common.compose.inputs.SearchInputField
import com.civilcam.ui.network.main.content.SearchRow

@Composable
fun LocationSelectContent(
    searchData: SearchModel,
    onAction: (Any) -> Unit,
) {
	var searchString by remember { mutableStateOf("") }

	Column(
		modifier = Modifier
            .fillMaxSize()
            .background(CCTheme.colors.white)
	) {
		Spacer(modifier = Modifier.height(32.dp))

		SearchInputField(
            isLetters = false,
            onValueChanged = {
                searchString = it
                onAction.invoke(it)
            }) {
		}

		LazyColumn {
			itemsIndexed(
				searchData.searchResult,
				key = { _, item -> item.placeId }) { index, item ->
				SearchRow(
                    title = "${item.primary} ${item.secondary}",
                    searchPart = searchString,
                    needDivider = index < searchData.searchResult.lastIndex,
                    rowClick = { onAction.invoke(item) },
                )
			}

			item {
				if (searchData.searchResult.isNotEmpty()) RowDivider()
			}
		}
	}
}