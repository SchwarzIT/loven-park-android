package com.schwarzit.lovenpark.profile.signals.view.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.schwarzit.lovenpark.BaseGoogleMapFragment
import com.schwarzit.lovenpark.databinding.FragmentSignalDetailsMapBinding
import com.schwarzit.lovenpark.signal.LATITUDE
import com.schwarzit.lovenpark.signal.LONGITUDE

class SignalDetailsMapFragment : BaseGoogleMapFragment() {
    private lateinit var binding: FragmentSignalDetailsMapBinding
    private var signalLocation: LatLng? = null

    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    override val callback = OnMapReadyCallback { googleMap ->
        signalLocation?.let {
            MarkerOptions()
                .position(it)
                .icon(getPin())
        }?.let { googleMap.addMarker(it) }

        val signalPosition = signalLocation?.let {
            CameraPosition.Builder()
                .target(it)
                .zoom(15f)
                .build()
        }

        signalPosition?.let {
            CameraUpdateFactory
                .newCameraPosition(it)
        }?.let { googleMap.animateCamera(it) }

        googleMap.uiSettings.setAllGesturesEnabled(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLocationFromSignalDetailsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignalDetailsMapBinding.inflate(inflater, container, false)
        binding?.root?.addView(super.onCreateView(inflater, container, savedInstanceState))
        hideUserLocationButton()

        getLocationFromSignalDetailsFragment()

        return binding?.root
    }

    private fun getLocationFromSignalDetailsFragment() {
        val latitude = arguments?.getDouble(LATITUDE)
        val longitude = arguments?.getDouble(LONGITUDE)

        if (latitude != null && longitude != null) {
            signalLocation = LatLng(latitude, longitude)
        }
    }
}