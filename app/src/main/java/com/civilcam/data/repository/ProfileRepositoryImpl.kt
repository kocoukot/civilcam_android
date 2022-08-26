package com.civilcam.data.repository

import android.net.Uri
import com.civilcam.data.ext.toPart
import com.civilcam.data.local.AccountStorage
import com.civilcam.data.local.MediaStorage
import com.civilcam.data.mapper.auth.UserBaseInfoMapper
import com.civilcam.data.mapper.profile.UserInfoToDomainMapper
import com.civilcam.data.network.model.request.profile.ChangePhoneNumberRequest
import com.civilcam.data.network.service.ProfileService
import com.civilcam.data.network.support.BaseRepository
import com.civilcam.data.network.support.Resource
import com.civilcam.domainLayer.model.profile.UserSetupModel
import com.civilcam.domainLayer.model.user.UserBaseInfo
import com.civilcam.domainLayer.repos.AccountRepository
import com.civilcam.domainLayer.repos.ProfileRepository

class ProfileRepositoryImpl(
    private val profileService: ProfileService,
    private val mediaStorage: MediaStorage,
    private val accountStorage: AccountStorage,
    private val accountRepository: AccountRepository
) : ProfileRepository, BaseRepository() {

    private val userBaseInfoMapper = UserBaseInfoMapper()

    private val userInfoToDomainMapper = UserInfoToDomainMapper()

    override suspend fun getUserProfile(): UserBaseInfo =
        safeApiCall {
            profileService.getUserProfile()
        }.let { response ->
            when (response) {
                is Resource.Success -> userBaseInfoMapper.mapData(response.value.profile)
                is Resource.Failure -> {
                    if (response.serviceException.isForceLogout) accountStorage.logOut()
                    throw response.serviceException
                }
            }
        }


    override suspend fun setUserProfile(userProfile: UserSetupModel): Boolean =
        safeApiCall {
            profileService.setUserProfile(userInfoToDomainMapper.mapData(userProfile))
        }.let { response ->
            when (response) {
                is Resource.Success -> true
                is Resource.Failure -> {
                    if (response.serviceException.isForceLogout) accountStorage.logOut()
                    throw response.serviceException
                }
            }
        }
    
    override suspend fun updateUserProfile(userProfile: UserSetupModel): Boolean =
        safeApiCall {
            profileService.updateProfile(userInfoToDomainMapper.mapData(userProfile))
        }.let { response ->
            when (response) {
                is Resource.Success -> true
                is Resource.Failure -> {
                    if (response.serviceException.isForceLogout) accountStorage.logOut()
                    throw response.serviceException
                }
            }
        }
    
    override suspend fun editPhoneNumber(phone: String) {
        safeApiCall {
            profileService.changeUserPhone(ChangePhoneNumberRequest(phone))
        }.let { response ->
            when (response) {
                is Resource.Success -> true
                is Resource.Failure -> {
                    if (response.serviceException.isForceLogout) accountStorage.logOut()
                    throw response.serviceException
                }
            }
        }
    }

    override suspend fun setUserAvatar(uri: Uri) {
        safeApiCall {
            val image = mediaStorage.getImageFile(uri).blockingGet()
            profileService.setUserAvatar(image.toPart("avatar"))
        }.let { response ->
            when (response) {
                is Resource.Success -> userBaseInfoMapper.mapData(response.value.profile)
                is Resource.Failure -> {
                    if (response.serviceException.isForceLogout) accountStorage.logOut()
                    throw response.serviceException
                }
            }
        }
    }

    override suspend fun deleteUserAvatar() {
        safeApiCall {
            profileService.deleteUserAvatar()
        }.let { response ->
            when (response) {
                is Resource.Success -> {}
                is Resource.Failure -> {
                    if (response.serviceException.isForceLogout) accountStorage.logOut()
                    throw response.serviceException
                }
            }
        }
    }


}