package com.schwarzit.lovenpark.mappin.ui.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.schwarzit.lovenpark.BaseGoogleMapFragment
import com.schwarzit.lovenpark.FeatureFlags
import com.schwarzit.lovenpark.PathsHelper
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.data.utils.UserSharedPrefHelper
import com.schwarzit.lovenpark.databinding.FragmentGoogleMapBinding
import com.schwarzit.lovenpark.locationlist.LocationInfoWindowFragment
import com.schwarzit.lovenpark.locationlist.Option
import com.schwarzit.lovenpark.mappin.domain.MapPinUIModel
import com.schwarzit.lovenpark.mappin.domain.PinCategory
import com.schwarzit.lovenpark.mappin.domain.getMapPinCategoryIcon
import com.schwarzit.lovenpark.mappin.ui.viewmodel.MapPinsViewModel
import com.schwarzit.lovenpark.user.socialLogin.SocialUserSharedPrefs
import com.schwarzit.lovenpark.utils.Util.Companion.COLOR_PREFIX
import com.schwarzit.lovenpark.utils.Util.Companion.EMPTY_STRING
import com.schwarzit.lovenpark.utils.Util.Companion.ZOOM_PREFERENCE_CLUSTER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoogleMapFragment : BaseGoogleMapFragment() {

    private var binding: FragmentGoogleMapBinding? = null

    private val mapPinsViewModel: MapPinsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoogleMapBinding.inflate(inflater, container, false)
        binding?.root?.addView(super.onCreateView(inflater, container, savedInstanceState))

        binding?.addSignalButton?.setOnClickListener {
            if (isUserLogged() == true) {
                navigateToSignalRoot()
            } else {
                checkNavigationFeatureFlagStatus()
            }
        }

        checkConnection?.observe(viewLifecycleOwner) { isConnected ->
            context?.let {
                if (isConnected) {
                    mapPinsViewModel.requestMapPinsWhenOnline(getCurrentLanguage() ,true)
                    mapPinsViewModel.getUserWhenOnline()
                } else {
                    mapPinsViewModel.requestMapPinsWhenOffline()
                    SocialUserSharedPrefs.removeUser(it)
                }
            }
        }

        mapPinsViewModel.liveDataMapPins.observe(viewLifecycleOwner) {
            if (gMap != null) {
                gMap?.clear()
                mapPinsViewModel.filterPinsFromSelectedCategories()?.let { it1 ->
                    gMap = context?.let { it2 -> PathsHelper.addPaths(gMap, context = it2) }
                    showAllPointsOnMap(it1)
                }
            }
        }

        mapPinsViewModel.selectedPinCategories.observe(viewLifecycleOwner) {
            if (gMap != null) {
                gMap?.clear()
                mapPinsViewModel.filterPinsFromSelectedCategories()?.let { it1 ->
                    gMap = context?.let { it2 -> PathsHelper.addPaths(gMap, context = it2) }
                    showAllPointsOnMap(it1)
                }
            }
        }
        return binding?.root
    }
    private fun getCurrentLanguage(): String = ConfigurationCompat
        .getLocales(Resources.getSystem().configuration).get(0).toString().split("_")[0]

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    private fun showAllPointsOnMap(pointsList: List<MapPinUIModel>) {
        val favouriteMapPins = mapPinsViewModel.getFavouriteMapPinsFromDatabase()
        pointsList.forEach { mapPin ->
            if (favouriteMapPins.contains(mapPin.id))
                mapPin.isBookmarked = true
        }
        clusterManager?.apply {
            clearItems()
            addItems(pointsList)
            gMap?.let {
                renderer = MarkerClusterRenderer(
                    context,
                    it,
                    clusterManager,
                )
            }
            cluster()
        }
    }

    private fun isUserLogged() =
        if (FeatureFlags.FLAG_LOGIN_REGISTRATION.isActive) {
            UserSharedPrefHelper.getLoggedUserEmail(requireContext())?.isNotEmpty()
        } else {
            SocialUserSharedPrefs.getLoggedUser(requireContext())?.isNotEmpty()
        }

    private fun checkNavigationFeatureFlagStatus() =
        if (FeatureFlags.FLAG_LOGIN_REGISTRATION.isActive) {
            navigateToLoginRegistrationFragment()
        } else {
            navigateToSocialLoginFragment()
        }

    private fun navigateToSignalRoot() {
        findNavController().navigate(GoogleMapFragmentDirections.actionGoogleMapFragmentToSignalRootFragment())
    }

    private fun navigateToSocialLoginFragment() {
        findNavController().navigate(GoogleMapFragmentDirections.actionGoogleMapFragmentToSocialLoginFragment())
    }

    private fun navigateToLoginRegistrationFragment() {
        findNavController().navigate(GoogleMapFragmentDirections.actionGoogleMapFragmentToLoginRegistrationFragment())
    }

    /*
            * In order to have custom implementation of click events
            * and customise the view of cluster and clustered pins
            * we need to create custom cluster renderer.
            **/
    inner class MarkerClusterRenderer(
        context: Context?,
        map: GoogleMap?,
        clusterManager: ClusterManager<MapPinUIModel?>?
    ) : DefaultClusterRenderer<MapPinUIModel>(context, map, clusterManager) {
        private val iconGenerator: IconGenerator
        private val markerImageView: ImageView

        init {
            iconGenerator = IconGenerator(context)
            markerImageView = ImageView(context)
            iconGenerator.setContentView(markerImageView)
        }

        override fun onBeforeClusterItemRendered(
            mapPin: MapPinUIModel,
            markerOptions: MarkerOptions
        ) {
            markerImageView.setImageResource(mapPin.getMapPinCategoryIcon())
            iconGenerator.setBackground(null)
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()))
        }

        override fun onBeforeClusterRendered(
            cluster: Cluster<MapPinUIModel>,
            markerOptions: MarkerOptions
        ) {
            super.onBeforeClusterRendered(cluster, markerOptions)
        }

        override fun getColor(clusterSize: Int): Int {
            val colorCyberGrapeAsString = String.format(
                COLOR_PREFIX,
                context?.let {
                    ContextCompat.getColor(it, R.color.loven_base_green)
                        .and(ContextCompat.getColor(it, R.color.white))
                }
                    ?: EMPTY_STRING
            )
            return Color.parseColor(colorCyberGrapeAsString)
        }

        override fun setOnClusterItemClickListener(listener: ClusterManager.OnClusterItemClickListener<MapPinUIModel>?) {
            val clusterManagerListener: ClusterManager.OnClusterItemClickListener<MapPinUIModel?> =
                ClusterManager.OnClusterItemClickListener<MapPinUIModel?> { item ->
                    mapPinsViewModel.liveDataMapPins.value?.firstOrNull { marker ->
                        item?.id == marker.id
                    }?.let { currentSelectedPin ->
                        val selectedPinIcon = setSelectedPinIcon(currentSelectedPin)
                        markerImageView.setImageResource(selectedPinIcon)
                        iconGenerator.setBackground(null)
                        getMarker(currentSelectedPin)?.setIcon(
                            BitmapDescriptorFactory.fromBitmap(
                                iconGenerator.makeIcon()
                            )
                        )
                        mapPinsViewModel.saveLastSelectedMapPin(currentSelectedPin)
                        showLocationInfoWindowOnTop(currentSelectedPin)
                    }
                    true
                }

            super.setOnClusterItemClickListener(clusterManagerListener)
        }

        private fun showLocationInfoWindowOnTop(currentSelectedPin: MapPinUIModel) {
            val modalBottomSheet = LocationInfoWindowFragment()
                .apply {
                    setOnOptionClickListener { option ->
                        when (option) {
                            Option.CREATE_SIGNAL -> navigateToSignalRoot()
                            Option.SAVE_TO_FAVOURITE -> { }
                            Option.SOCIAL_LOGIN_REGISTER -> navigateToSocialLoginFragment()
                            Option.GENERIC_LOGIN_REGISTER -> navigateToLoginRegistrationFragment()
                        }
                    }
                }
            activity?.supportFragmentManager?.let {
                modalBottomSheet.show(
                    it,
                    LocationInfoWindowFragment.TAG
                )
            }
            modalBottomSheet.setLastSelectedMarker(getMarker(currentSelectedPin))
        }

        private fun setSelectedPinIcon(currentSelectedPin: MapPinUIModel) =
            when (currentSelectedPin.category) {
                PinCategory.BENCH -> R.drawable.ic_pin_bench_selected
                PinCategory.BICYCLE -> R.drawable.ic_pin_bike_selected
                PinCategory.TRASH -> R.drawable.ic_pin_trashcan_selected
                PinCategory.OTHER -> R.drawable.ic_pin_tree_selected
            }

        override fun setOnClusterClickListener(listener: ClusterManager.OnClusterClickListener<MapPinUIModel>?) {
            val cameraUpdate: ClusterManager.OnClusterClickListener<MapPinUIModel> =
                ClusterManager.OnClusterClickListener<MapPinUIModel> { selectedCluster ->
                    gMap?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            selectedCluster.position,
                            ZOOM_PREFERENCE_CLUSTER
                        )
                    )
                    true
                }
            super.setOnClusterClickListener(cameraUpdate)
        }
    }

}

