package com.schwarzit.lovenpark.mappin.data.remote

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.schwarzit.lovenpark.mappin.data.local.MapPinRealm
import com.schwarzit.lovenpark.mappin.domain.MapPinUIModel
import com.schwarzit.lovenpark.mappin.domain.getPinCategoryEnum

data class MapPinModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("poiCategory")
    val category: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("pictures")
    val pictures: List<PictureModel>? = emptyList(),
    @SerializedName("isBookmarked")
    val isBookmarked : Boolean = false,
)

fun MapPinModel.toMapPinRealm(): MapPinRealm {
    val mapPinRealm = MapPinRealm()
    this.apply {
        mapPinRealm.id = id
        mapPinRealm.latitude = latitude
        mapPinRealm.longitude = longitude
        mapPinRealm.category = category
        mapPinRealm.description = description
        mapPinRealm.title = title
        if (pictures != null) {
            mapPinRealm.pictures = pictures
        }
        mapPinRealm.isBookmarked = isBookmarked
    }
    return mapPinRealm
}

fun MapPinModel.toMapPinUIModel() = MapPinUIModel(
    id,
    LatLng(latitude, longitude),
    category.getPinCategoryEnum(),
    description,
    title,
    pictures,
    isBookmarked
)

