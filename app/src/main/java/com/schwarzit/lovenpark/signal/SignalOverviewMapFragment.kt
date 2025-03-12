package com.schwarzit.lovenpark.signal

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
import com.schwarzit.lovenpark.databinding.FragmentSignalOverviewMapBinding

class SignalOverviewMapFragment : BaseGoogleMapFragment() {
    private var binding: FragmentSignalOverviewMapBinding? = null
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
        getLocationFromSignalRootFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignalOverviewMapBinding.inflate(inflater, container, false)
        hideUserLocationButton()
        binding?.root?.addView(super.onCreateView(inflater, container, savedInstanceState))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideUserLocationButton()
    }

    private fun getLocationFromSignalRootFragment() {
        val lat = arguments?.getDouble(LATITUDE)
        val lon = arguments?.getDouble(LONGITUDE)
        signalLocation =
            lat?.let { latitude -> lon?.let { longitude -> LatLng(latitude, longitude) } }
    }

}