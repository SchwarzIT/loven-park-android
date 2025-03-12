package com.schwarzit.lovenpark.signal.signalmodel

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("name")
    val name: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("mimeType")
    val mimeType: String?
)