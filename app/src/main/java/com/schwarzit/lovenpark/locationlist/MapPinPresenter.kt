package com.schwarzit.lovenpark.locationlist

import com.schwarzit.lovenpark.mappin.domain.MapPinUIModel

interface MapPinPresenter {
    fun onMapPinClick(selectedMapPin: MapPinUIModel)
}