package com.schwarzit.lovenpark.user

import com.google.gson.annotations.SerializedName

data class RefreshTokenInfo(
    @SerializedName("access_token") val accessToken: String,
)
