package com.schwarzit.lovenpark.locationlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.schwarzit.lovenpark.FeatureFlags
import com.schwarzit.lovenpark.MainActivity
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.data.utils.UserSharedPrefHelper
import com.schwarzit.lovenpark.databinding.FragmentLocationDetailBinding
import com.schwarzit.lovenpark.mappin.data.remote.PictureModel
import com.schwarzit.lovenpark.mappin.domain.MapPinUIModel
import com.schwarzit.lovenpark.mappin.domain.PinCategory
import com.schwarzit.lovenpark.mappin.ui.viewmodel.MapPinsViewModel
import com.schwarzit.lovenpark.user.socialLogin.SocialUserSharedPrefs
import com.schwarzit.lovenpark.utils.Util.Companion.EMPTY_STRING

class LocationDetailFragment : Fragment() {

    private var binding: FragmentLocationDetailBinding? = null
    private var adapter: LocationPicturesAdapter = LocationPicturesAdapter()
    private val mapPinsViewModel: MapPinsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationDetailBinding.inflate(inflater, container, false)
        binding?.apply {
            createSignal.setOnClickListener {
                if (isUserLogged() == true) {
                    navigateToSignalRoot()
                } else {
                    checkNavigationFeatureFlagStatus()
                }
            }
            goToLocation.setOnClickListener {
                navigateToGoogleMap()
            }
        }

        mapPinsViewModel.selectedPin.observe(viewLifecycleOwner) { mapPin ->
            setSelectedMapPinDetails(mapPin)
        }

        return binding?.root
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setLocationsPictures(emptyList())
        binding?.locationPictures?.adapter = adapter
    }

    private fun setSelectedMapPinDetails(selectedMapPin: MapPinUIModel) {
        (activity as MainActivity).findViewById<TextView>(R.id.fragment_title)?.text = selectedMapPin.pinTitle

        binding?.apply {
            locationInfoText.text = selectedMapPin.description
            locationCategory.text =
                when (selectedMapPin.category) {
                    PinCategory.BENCH -> getString(R.string.map_pin_title_bench)
                    PinCategory.BICYCLE -> getString(R.string.map_pin_title_bicycle)
                    PinCategory.TRASH -> getString(R.string.map_pin_title_trash)
                    else -> getString(R.string.map_pin_title_other)
                }
            if (selectedMapPin.picture?.isEmpty() == true) {
                adapter.setLocationsPictures(listOf(PictureModel(EMPTY_STRING)))
            } else {
                selectedMapPin.picture?.let { adapter.setLocationsPictures(it) }
            }
            locationPictures.adapter = adapter
        }
    }

    private fun navigateToGoogleMap() {
        findNavController().navigate(LocationDetailFragmentDirections.actionLocationDetailFragmentToGoogleMapFragment())
    }

    private fun navigateToSignalRoot() {
        findNavController().navigate(LocationDetailFragmentDirections.actionLocationDetailFragmentToSignalRootFragment())
    }

    private fun navigateToSocialLoginFragment() {
        findNavController().navigate(LocationDetailFragmentDirections.actionLocationDetailFragmentToSocialLoginFragment())
    }

    private fun navigateToLoginRegistrationFragment() {
        findNavController().navigate(LocationDetailFragmentDirections.actionLocationDetailFragmentToLoginRegistrationFragment())
    }
}