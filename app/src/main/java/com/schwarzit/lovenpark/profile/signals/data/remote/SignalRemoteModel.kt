package com.schwarzit.lovenpark.profile.signals.data.remote

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.schwarzit.lovenpark.mappin.data.remote.PictureModel
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalCategory
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalModel
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalStatus
import com.schwarzit.lovenpark.profile.signals.data.local.SignalRealm
import com.schwarzit.lovenpark.utils.Util
import com.schwarzit.lovenpark.utils.Util.Companion.SIGNAL_DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*

data class SignalRemoteModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("createdAt")
    val createdAt: Date,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("description")
    val description: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("pictures")
    var pictures: List<PictureModel>? = emptyList()
)

//todo process all fields
@SuppressLint("SimpleDateFormat")
fun SignalRemoteModel.toSignalModel(): SignalModel {
    val mCreatedAt: String = if (createdAt != null)
        SimpleDateFormat(SIGNAL_DATE_PATTERN).format(createdAt) else ""


    val mSignalCategory: SignalCategory =
        when (this.category) {
            SignalCategory.ANIMAL_CARCASSES_FOUND.name -> SignalCategory.ANIMAL_CARCASSES_FOUND
            SignalCategory.DAMAGED_PARK_FURNITURE.name -> SignalCategory.DAMAGED_PARK_FURNITURE
            SignalCategory.INJURED_TREE.name -> SignalCategory.INJURED_TREE
            SignalCategory.UNCOLLECTED_WASTE.name -> SignalCategory.UNCOLLECTED_WASTE
            else -> SignalCategory.OTHER
        }

    val mSignalStatus: SignalStatus =
        if (this.status == SignalStatus.NEW.name) SignalStatus.NEW else SignalStatus.DONE


    return SignalModel(
        this.id,
        getShortenedNumber(),
        mCreatedAt,
        this.latitude,
        this.longitude,
        mSignalStatus,
        mSignalCategory,
        this.description,
        pictures ?: emptyList(),
    )
}

@SuppressLint("SimpleDateFormat")
fun SignalRemoteModel.toSignalRealm(): SignalRealm {

    val mCreatedAt: String = if (createdAt != null)
        SimpleDateFormat(SIGNAL_DATE_PATTERN).format(createdAt) else ""

    val mSignalCategory: SignalCategory =
        when (this.category) {
            SignalCategory.ANIMAL_CARCASSES_FOUND.name -> SignalCategory.ANIMAL_CARCASSES_FOUND
            SignalCategory.DAMAGED_PARK_FURNITURE.name -> SignalCategory.DAMAGED_PARK_FURNITURE
            SignalCategory.INJURED_TREE.name -> SignalCategory.INJURED_TREE
            SignalCategory.UNCOLLECTED_WASTE.name -> SignalCategory.UNCOLLECTED_WASTE
            else -> SignalCategory.OTHER
        }

    val mSignalStatus: SignalStatus =
        if (this.status == SignalStatus.NEW.name) SignalStatus.NEW else SignalStatus.DONE

    val signalRealm = SignalRealm()
    this.apply {
        signalRealm.id = id
        signalRealm.number = getShortenedNumber()
        signalRealm.createdAt = mCreatedAt
        signalRealm.latitude = latitude
        signalRealm.longitude = longitude
        signalRealm.status =
            mSignalStatus.name
        signalRealm.category = mSignalCategory.name
        signalRealm.description = description

        if (pictures != null) {
            signalRealm.pictures = pictures as List<PictureModel>
        }
    }
    return signalRealm

}

/*
Returns the shortened version of the signal number, which consists of the digits at indexes from 2 to 8, and from indexes 14 to the end of the string
 */
fun SignalRemoteModel.getShortenedNumber(): String {
    val numberRange1 = this.number.subSequence(
        Util.SIGNAL_NUMBER_RANGE1_START,
        Util.SIGNAL_NUMBER_RANGE1_END
    )
    val numberRange2 = this.number.subSequence(Util.SIGNAL_NUMBER_RANGE2_START, number.length)

    val sbShortenedNumber = StringBuilder().append(numberRange1).append(numberRange2)
    return sbShortenedNumber.toString()
}


