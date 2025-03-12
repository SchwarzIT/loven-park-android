package com.schwarzit.lovenpark.mappin.domain

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.mappin.data.remote.MapPinModel
import com.schwarzit.lovenpark.mappin.data.remote.PictureModel

data class MapPinUIModel(
    val id: String,
    val location: LatLng,
    val category: PinCategory,
    val description: String,
    val pinTitle: String,
    val picture: List<PictureModel>?,
    var isBookmarked : Boolean = false,
) : ClusterItem {
    override fun getPosition() = location

    override fun getTitle(): String? = null

    override fun getSnippet(): String? = null
}

fun MapPinUIModel.getMapPinCategoryIcon() =
    when (this.category) {
        PinCategory.BENCH -> R.drawable.ic_pin_bench_unselected
        PinCategory.BICYCLE -> R.drawable.ic_pin_bike_unselected
        PinCategory.TRASH -> R.drawable.ic_pin_trashcan_unselected
        PinCategory.OTHER -> R.drawable.ic_pin_tree_unselected
    }

fun MapPinUIModel.toMapPinModel() =
    MapPinModel(
        id = id,
        title = pinTitle,
        description = description,
        category = category.name,
        latitude = location.latitude,
        longitude = location.longitude,
        pictures = picture,
        isBookmarked = isBookmarked,
    )
