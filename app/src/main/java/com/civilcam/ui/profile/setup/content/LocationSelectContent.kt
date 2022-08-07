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
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.SearchModel
import com.civilcam.ui.common.compose.RowDivider
import com.civilcam.ui.common.compose.inputs.SearchInputField
import com.civilcam.ui.network.main.content.SearchRow
import com.civilcam.ui.profile.setup.model.ProfileSetupActions
import com.civilcam.ui.profile.userProfile.model.UserProfileActions

@Composable
fun LocationSelectContent(
	searchData: SearchModel,
	isEdit: Boolean,
	locationAction: (ProfileSetupActions) -> Unit,
	editLocationAction: (UserProfileActions) -> Unit
) {
	var searchString by remember { mutableStateOf("") }
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(CCTheme.colors.white)
	) {
		Spacer(modifier = Modifier.height(32.dp))
		
		SearchInputField(
			onValueChanged = {
				searchString = it
				if (isEdit) {
					editLocationAction.invoke(UserProfileActions.LocationSearchQuery(it))
				} else {
					locationAction.invoke(ProfileSetupActions.LocationSearchQuery(it))
				}
			}) {
			
		}
		
		LazyColumn {
			itemsIndexed(searchData.searchResult) { index, item ->
				SearchRow(
					title = "${item.primary} ${item.secondary}",
					searchPart = searchString,
					needDivider = index < searchData.searchResult.lastIndex,
					rowClick = {
						if (isEdit) {
							editLocationAction(UserProfileActions.ClickAddressSelect(item))
						} else {
							locationAction(ProfileSetupActions.ClickAddressSelect(item))
						}
					},
				)
			}
			
			item {
				if (searchData.searchResult.isNotEmpty()) RowDivider()
			}
		}
	}
}