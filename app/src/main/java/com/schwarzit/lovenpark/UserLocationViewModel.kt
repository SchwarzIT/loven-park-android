package com.schwarzit.lovenpark

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.schwarzit.lovenpark.utils.Util.Companion.PARK_BOUNDS

class UserLocationViewModel : ViewModel() {

    var estimateLocation = MutableLiveData<LatLng>()

    fun hasLocationPermission(context: Context) =
        (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)

}

fun LatLng.isUserLocationInPark() =
    PolyUtil.containsLocation(this, PARK_BOUNDS, false)
