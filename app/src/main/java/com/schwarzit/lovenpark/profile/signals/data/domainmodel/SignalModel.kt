package com.schwarzit.lovenpark.profile.signals.data.domainmodel

import com.schwarzit.lovenpark.mappin.data.remote.PictureModel

data class SignalModel(
    val id: String,
    val number: String,
    val createdAt: String,
    val latitude: Double,
    val longitude: Double,
    val status: SignalStatus,
    val category: SignalCategory,
    val description: String,
    val pictures: List<PictureModel>?,
)

