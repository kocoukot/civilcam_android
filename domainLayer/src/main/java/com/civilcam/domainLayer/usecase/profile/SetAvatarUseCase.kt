package com.civilcam.domainLayer.usecase.profile

import android.net.Uri
import com.civilcam.domainLayer.repos.ProfileRepository

class SetAvatarUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend fun invoke(uri: Uri) = profileRepository.setUserAvatar(uri)
}