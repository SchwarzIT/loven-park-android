package com.schwarzit.lovenpark.mappin.data.remote

import com.google.gson.annotations.SerializedName
import com.schwarzit.lovenpark.mappin.data.local.FavouriteMapPinRealm

data class FavouriteMapPinModel(
    @SerializedName("id")
    val id: String,
)

fun FavouriteMapPinModel.toFavouriteMapPinRealm() =
    FavouriteMapPinRealm().apply {
        this.id = id
    }

