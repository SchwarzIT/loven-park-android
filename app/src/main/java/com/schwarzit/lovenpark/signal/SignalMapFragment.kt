package com.schwarzit.lovenpark.signal

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.schwarzit.lovenpark.BaseGoogleMapFragment
import com.schwarzit.lovenpark.PathsHelper
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.FragmentSignalMapBinding
import com.schwarzit.lovenpark.isUserLocationInPark
import com.schwarzit.lovenpark.utils.Util.Companion.LOVEN_PARK_LOCATION

class SignalMapFragment : BaseGoogleMapFragment() {
    private var binding: FragmentSignalMapBinding? = null
    private lateinit var client: FusedLocationProviderClient
    private var marker: Marker? = null
    private var isUserInPark: Boolean? = null

    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    override val callback = OnMapReadyCallback { googleMap ->
        gMap = googleMap
        gMap = context?.let { PathsHelper.addPaths(gMap, context = it) }
        setMapBoundaries(googleMap)
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (context?.let { viewModel.hasLocationPermission(it) } == true
            && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        ) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            marker?.remove()
            getCurrentLocation()
        } else {
            marker = if (marker == null) {
                addMarker(googleMap, LOVEN_PARK_LOCATION)
            } else {
                marker?.remove()
                viewModel.estimateLocation.let {
                    it.value?.let { userLocation ->
                        addMarker(
                            googleMap,
                            userLocation
                        )
                    }
                }
            }
        }
        googleMap.apply {
            uiSettings.isCompassEnabled = false
            uiSettings.isMapToolbarEnabled = false
        }
        setSignalPositionOnMap(googleMap)
        getSavedLocation()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignalMapBinding.inflate(inflater, container, false)
        binding?.root?.addView(super.onCreateView(inflater, container, savedInstanceState))
        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        userLocation().observe(viewLifecycleOwner) { userLocation: LatLng? ->
            isUserInPark = userLocation?.isUserLocationInPark()
        }
        return binding?.root
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation() {
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            client.lastLocation.addOnCompleteListener { task ->
                val location = task.result
                if (location != null) {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    marker = if (marker == null) {
                        addMarker(gMap, userLocation)
                    } else {
                        viewModel.estimateLocation.let {
                            marker?.remove()
                            it.value?.let { userLocation ->
                                addMarker(
                                    gMap,
                                    userLocation
                                )
                            }
                        }
                    }
                    setSignalPositionOnMap(gMap)
                    getSavedLocation()
                }
            }
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }


    private fun getSavedLocation() {
        val latitude = arguments?.getFloat(LATITUDE)
        val longitude = arguments?.getFloat(LONGITUDE)
        if (latitude != null && longitude != null && latitude > 0 && longitude > 0) {
            marker?.remove()
            marker = addMarker(gMap, LatLng(latitude.toDouble(), longitude.toDouble()))
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun setSignalPositionOnMap(map: GoogleMap?) {
        map?.setOnMapClickListener { onClickLocation ->
            marker?.remove()
            marker = addMarker(map, onClickLocation)
            viewModel.estimateLocation.value = onClickLocation
            passLocationDataToSignalRootFragment(onClickLocation)
            map.snapshot { snapshot ->
                if (snapshot != null) {
                    setFragmentResult(
                        MAP_SIGNAL_SCREENSHOT_KEY,
                        bundleOf(MAP_SIGNAL_SCREENSHOT to snapshot)
                    )
                }
            }
        }

        map?.setOnMarkerDragListener(
            object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDragStart(marker: Marker) {
                }

                override fun onMarkerDragEnd(marker: Marker) {
                    viewModel.estimateLocation.value = marker.position
                    passLocationDataToSignalRootFragment(marker.position)
                    map.animateCamera(CameraUpdateFactory.newLatLng(marker.position))
                    map.snapshot { snapshot ->
                        if (snapshot != null) {
                            setFragmentResult(
                                MAP_SIGNAL_SCREENSHOT_KEY,
                                bundleOf(MAP_SIGNAL_SCREENSHOT to snapshot)
                            )
                        }
                    }
                }

                override fun onMarkerDrag(marker: Marker) {
                }
            }
        )
    }

    private fun passLocationDataToSignalRootFragment(location: LatLng) {
        setFragmentResult(KEY, bundleOf(LOCATION to location))
        setFragmentResult(IS_USER_IN_PARK_KEY, bundleOf(IS_USER_IN_PARK to isUserInPark))
    }
}