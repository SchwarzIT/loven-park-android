package com.schwarzit.lovenpark.profile.data.remote

import com.google.gson.annotations.SerializedName

data class UpdateUserInfo (
    @SerializedName("email")val email: String,
    @SerializedName("fullName")val fullName: String,
    @SerializedName("phoneNumber")val phoneNumber: String
)