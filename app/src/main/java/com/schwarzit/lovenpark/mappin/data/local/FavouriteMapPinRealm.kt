package com.schwarzit.lovenpark.mappin.data.local

import com.schwarzit.lovenpark.utils.Util
import io.realm.kotlin.types.RealmObject
import io.realm.annotations.PrimaryKey

open class FavouriteMapPinRealm : RealmObject {
    @PrimaryKey
    var id = Util.EMPTY_STRING
}

fun String.toFavouriteMapPinRealm(): FavouriteMapPinRealm =
    FavouriteMapPinRealm().apply { id = this@toFavouriteMapPinRealm }

