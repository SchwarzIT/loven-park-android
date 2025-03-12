package com.schwarzit.lovenpark.profile.signals.data.local

import com.schwarzit.lovenpark.mappin.data.remote.PictureModel
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalCategory
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalModel
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalStatus
import com.schwarzit.lovenpark.utils.Util
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.types.RealmObject

open class SignalRealm : RealmObject {
    @PrimaryKey
    var id = Util.EMPTY_STRING
    var number = Util.EMPTY_STRING
    var createdAt = Util.EMPTY_STRING
    var latitude = Util.ZERO_DOUBLE
    var longitude = Util.ZERO_DOUBLE
    var status = Util.EMPTY_STRING
    var category = Util.EMPTY_STRING
    var description = Util.EMPTY_STRING
    var pictures = listOf<PictureModel>()
}

fun SignalRealm.toSignalModel() = SignalModel(
    id,
    number,
    createdAt,
    latitude,
    longitude,
    SignalStatus.valueOf(status),
    SignalCategory.valueOf(category),
    description,
    pictures,

)