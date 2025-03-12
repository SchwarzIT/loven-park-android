package com.schwarzit.lovenpark.signal.signalmodel.response

import com.google.gson.annotations.SerializedName

data class PictureResponse(
    @SerializedName("url")
    val url: String
)