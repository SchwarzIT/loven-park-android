package com.schwarzit.lovenpark.signal.signalmodel.response

import com.google.gson.annotations.SerializedName

data class SignalResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("pictures")
    val pictures: List<PictureResponse>
)