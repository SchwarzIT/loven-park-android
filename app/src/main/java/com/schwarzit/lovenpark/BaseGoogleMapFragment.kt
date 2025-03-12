package com.schwarzit.lovenpark

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.*
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import com.schwarzit.lovenpark.R.drawable.ic_map_pin
import com.schwarzit.lovenpark.databinding.FragmentBaseGoogleMapBinding
import com.schwarzit.lovenpark.mappin.domain.MapPinUIModel
import com.schwarzit.lovenpark.utils.Util.Companion.LOVEN_PARK_LOCATION
import com.schwarzit.lovenpark.utils.Util.Companion.LOVEN_PARK_NE_LAT
import com.schwarzit.lovenpark.utils.Util.Companion.LOVEN_PARK_NE_LON
import com.schwarzit.lovenpark.utils.Util.Companion.LOVEN_PARK_SW_LAT
import com.schwarzit.lovenpark.utils.Util.Companion.LOVEN_PARK_SW_LON
import com.schwarzit.lovenpark.utils.Util.Companion.ZOOM_PREFERENCE
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
open class BaseGoogleMapFragment : Fragment() {

    private var binding: FragmentBaseGoogleMapBinding? = null
    protected val viewModel: UserLocationViewModel by viewModels()
    private lateinit var client: FusedLocationProviderClient

    protected val checkConnection by lazy { context?.let { NetworkLiveData(it) } }
    var gMap: GoogleMap? = null

    protected var clusterManager: ClusterManager<MapPinUIModel?>? = null

    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    protected open val callback = OnMapReadyCallback { googleMap ->

        gMap = googleMap
        setMapBoundaries(googleMap)

        clusterManager = ClusterManager<MapPinUIModel?>(context, googleMap)

        googleMap.apply {
            isMyLocationEnabled = context?.let { viewModel.hasLocationPermission(it) } == true
            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isCompassEnabled = false
            uiSettings.isMapToolbarEnabled = false
            setOnCameraIdleListener(clusterManager)
            setOnMarkerClickListener(clusterManager)
        }
    }

    protected fun setMapBoundaries(googleMap: GoogleMap) {
        val lovenParkBounds = LatLngBounds(
            LatLng((LOVEN_PARK_SW_LAT), LOVEN_PARK_SW_LON),  // SW bounds
            LatLng((LOVEN_PARK_NE_LAT), LOVEN_PARK_NE_LON) // NE bounds
        )

        googleMap.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(lovenParkBounds.center, ZOOM_PREFERENCE))
            setLatLngBoundsForCameraTarget(lovenParkBounds)
            setMinZoomPreference(ZOOM_PREFERENCE)
            animateCamera(CameraUpdateFactory.zoomTo(ZOOM_PREFERENCE))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseGoogleMapBinding.inflate(inflater, container, false)
        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding?.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        userLocation().observe(viewLifecycleOwner) { userLocation: LatLng? ->
            binding?.btnUserLocation?.isEnabled =
                userLocation != null && userLocation.isUserLocationInPark()
        }

        binding?.btnUserLocation?.setOnClickListener {
            viewModel.estimateLocation.value.let { userLocation ->

                val locationManager =
                    activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (context?.let { viewModel.hasLocationPermission(it) } != true) {
                    requestLocationPermissions()
                }
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER).not()) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                if (context?.let { it1 -> viewModel.hasLocationPermission(it1) } == true
                    && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    gMap?.isMyLocationEnabled = true
                    gMap?.uiSettings?.isMyLocationButtonEnabled = false
                    userLocation?.let { location ->
                        CameraUpdateFactory.newLatLngZoom(
                            location,
                            ZOOM_PREFERENCE
                        )
                    }?.let { cameraUpdate -> gMap?.animateCamera(cameraUpdate) }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        clusterManager = null
    }

    override fun onResume() {
        super.onResume()
        if (clusterManager == null)
            clusterManager = ClusterManager<MapPinUIModel?>(context, gMap)
    }

    protected fun hideUserLocationButton() {
        binding?.btnUserLocation?.visibility = View.GONE
    }

    /**
     * Grants Access_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions.
     */
    private fun requestLocationPermissions() {
        val permissionFineLocation = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val permissionCoarseLocation = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (permissionFineLocation != PackageManager.PERMISSION_GRANTED || permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            permissionsResultCallback.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            permissionsResultCallback.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private val permissionsResultCallback =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    @SuppressLint("MissingPermission")
    protected fun userLocation(): MutableLiveData<LatLng> {
        client.lastLocation.addOnSuccessListener { userLocation ->
            viewModel.estimateLocation.value = if (userLocation != null)
                LatLng(userLocation.latitude, userLocation.longitude)
            else
                LOVEN_PARK_LOCATION
        }
        return viewModel.estimateLocation
    }

    /**
     * In order to use the my-location-layer feature you need to
     * request permission for either ACCESS_COARSE_LOCATION or
     * ACCESS_FINE_LOCATION unless you have set a custom location source.
     */
    @SuppressLint("MissingPermission")
    protected open fun getCurrentLocation() {
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    protected fun addMarker(map: GoogleMap?, position: LatLng) =
        map?.addMarker(MarkerOptions().position(position).draggable(true).icon(getPin()))

    private fun getBitmap(drawableRes: Int): Bitmap? {
        val drawable = activity?.let { ContextCompat.getDrawable(it, drawableRes) };
        val canvas = Canvas()
        val bitmap = drawable?.let {
            Bitmap.createBitmap(
                it.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        canvas.setBitmap(bitmap)
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable?.draw(canvas)
        return bitmap
    }

    protected fun getPin() = getBitmap(ic_map_pin)?.let { BitmapDescriptorFactory.fromBitmap(it) }
}
