package com.civilcam.data.network.model.response.profile

import com.civilcam.data.network.model.response.auth.UseBaseInfoResponse
import com.google.gson.annotations.SerializedName

class UserProfileResponse(
    @SerializedName("profile") val profile: UseBaseInfoResponse,
)
