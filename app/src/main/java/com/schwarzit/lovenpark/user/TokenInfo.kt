package com.schwarzit.lovenpark.user

import com.google.gson.annotations.SerializedName

data class TokenInfo(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String

)


