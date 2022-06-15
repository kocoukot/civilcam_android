package com.civilcam.arch.mvvm.viewmodel

data class OnAttachAction(
    val action: () -> Unit,
    val executeAtFirstAttach: Boolean
)