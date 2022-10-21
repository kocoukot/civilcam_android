package com.civilcam.ext_features.compose.elements

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun PhotoPickerContract(photoUri: (Uri) -> Unit) =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                photoUri.invoke(it)
            }
        })
