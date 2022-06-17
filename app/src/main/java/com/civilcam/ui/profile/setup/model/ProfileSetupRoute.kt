package com.civilcam.ui.profile.setup.model

sealed class ProfileSetupRoute {
    object GoSubscription : ProfileSetupRoute()
    object GoBack : ProfileSetupRoute()
    object OpenDatePicker : ProfileSetupRoute()
    object GoLocationSelect : ProfileSetupRoute()
    object GoGalleryOpen : ProfileSetupRoute()

}
