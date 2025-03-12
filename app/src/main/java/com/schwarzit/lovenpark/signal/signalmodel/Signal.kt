package com.schwarzit.lovenpark.signal.signalmodel

import com.google.gson.annotations.SerializedName
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalCategory

data class Signal(
    @SerializedName("category")
    val category: SignalCategory,
    @SerializedName("description")
    val description: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("pictures")
    val pictures: MutableList<Picture?>,
    @SerializedName("createdAt")
    val createdAt: String
)