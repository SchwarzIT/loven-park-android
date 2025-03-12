package com.schwarzit.lovenpark.mappin.data.local

import com.google.android.gms.maps.model.LatLng
import com.schwarzit.lovenpark.mappin.data.remote.PictureModel
import com.schwarzit.lovenpark.mappin.domain.MapPinUIModel
import com.schwarzit.lovenpark.mappin.domain.getPinCategoryEnum
import com.schwarzit.lovenpark.utils.Util.Companion.EMPTY_STRING
import com.schwarzit.lovenpark.utils.Util.Companion.ZERO_DOUBLE
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.types.RealmObject

open class MapPinRealm : RealmObject {
    @PrimaryKey
    var id = EMPTY_STRING
    var latitude = ZERO_DOUBLE
    var longitude = ZERO_DOUBLE
    var category = EMPTY_STRING
    var description = EMPTY_STRING
    var title = EMPTY_STRING
    var pictures = listOf<PictureModel>()
    var isBookmarked = false
}

fun MapPinRealm.toMapPinUIModel() = MapPinUIModel(
    id,
    LatLng(latitude, longitude),
    category.getPinCategoryEnum(),
    description,
    title,
    pictures,
    isBookmarked,
)
