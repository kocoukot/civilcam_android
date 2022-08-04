package com.civilcam.domain.usecase.profile

import android.net.Uri
import com.civilcam.data.repository.ProfileRepository

class SetAvatarUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend fun invoke(uri: Uri) = profileRepository.setUserAvatar(uri)
}