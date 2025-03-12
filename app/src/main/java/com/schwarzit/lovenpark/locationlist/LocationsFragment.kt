package com.schwarzit.lovenpark.locationlist

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.schwarzit.lovenpark.NetworkLiveData
import com.schwarzit.lovenpark.databinding.FragmentLocationsBinding
import com.schwarzit.lovenpark.mappin.domain.MapPinUIModel
import com.schwarzit.lovenpark.mappin.ui.viewmodel.MapPinsViewModel

class LocationsFragment : Fragment(), MapPinPresenter {

    private var binding: FragmentLocationsBinding? = null
    private var adapter: LocationsListAdapter = LocationsListAdapter()
    private val mapPinsViewModel: MapPinsViewModel by activityViewModels()
    protected val checkConnection by lazy { context?.let { NetworkLiveData(it) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationsBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapPinsViewModel.liveDataMapPins.observe(viewLifecycleOwner) {
            val favouriteMapPins = mapPinsViewModel.getFavouriteMapPinsFromDatabase()
            it.forEach { mapPin ->
                mapPin.isBookmarked = favouriteMapPins.contains(mapPin.id)
            }
            adapter.setLocationsList(sortLocationsBySelectedCategories())
            binding?.locationsList?.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        checkConnection?.observe(viewLifecycleOwner) { isConnected ->
            context?.let {
                if (isConnected) {
                    mapPinsViewModel.requestMapPinsWhenOnline(getCurrentLanguage() ,true)
                } else {
                    mapPinsViewModel.requestMapPinsWhenOffline()
                }
            }
        }
        mapPinsViewModel.selectedPinCategories.observe(viewLifecycleOwner) {
            adapter.setLocationsList(sortLocationsBySelectedCategories())
            adapter.onCreateSignalClick = {
                navigateToSignalRootFragment()
            }
            binding?.locationsList?.adapter = adapter
        }
    }

    private fun getCurrentLanguage(): String = ConfigurationCompat
        .getLocales(Resources.getSystem().configuration).get(0).toString().split("_")[0]


    private fun sortLocationsBySelectedCategories() =
        mapPinsViewModel.filterPinsFromSelectedCategories().orEmpty()


    override fun onMapPinClick(selectedMapPin: MapPinUIModel) {
        mapPinsViewModel.saveLastSelectedMapPin(selectedMapPin)
        //TODO: will rework in future to show the new pin detail page as pop-up on map screen
        navigateToLocationDetail()
    }

    private fun navigateToLocationDetail() {
        findNavController().navigate(LocationsFragmentDirections.actionLocationsFragmentToLocationDetailFragment())
    }

    private fun navigateToSignalRootFragment() {
        findNavController().navigate(LocationsFragmentDirections.actionLocationsFragmentToSignalRootFragment())
    }
}