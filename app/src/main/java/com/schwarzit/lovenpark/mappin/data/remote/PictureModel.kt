package com.schwarzit.lovenpark.mappin.data.remote

import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.annotations.SerializedName
import com.schwarzit.lovenpark.CoreConfig
import com.schwarzit.lovenpark.R.drawable.image_placeholder

data class PictureModel(
    @SerializedName("url")
    var url: String
)

fun View.setGlideRes(
    glideRes: String?,
) {
    val pictureUrl = if (glideRes?.contains(CoreConfig().lovenParkBaseUrl) == true) {
        glideRes
    } else {
        CoreConfig().lovenParkBaseUrl + glideRes
    }

    Glide.with(this)
        .load(pictureUrl)
        .placeholder(image_placeholder)
        .error(image_placeholder)
        .centerCrop()
        .into(this as ShapeableImageView)
}