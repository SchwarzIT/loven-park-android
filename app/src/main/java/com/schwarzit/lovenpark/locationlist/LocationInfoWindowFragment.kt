package com.schwarzit.lovenpark.locationlist

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.maps.android.ui.IconGenerator
import com.schwarzit.lovenpark.FeatureFlags
import com.schwarzit.lovenpark.MainActivity
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.data.utils.UserSharedPrefHelper
import com.schwarzit.lovenpark.databinding.FragmentLocationInfoWindowBinding
import com.schwarzit.lovenpark.mappin.data.remote.PictureModel
import com.schwarzit.lovenpark.mappin.domain.MapPinUIModel
import com.schwarzit.lovenpark.mappin.domain.PinCategory
import com.schwarzit.lovenpark.mappin.ui.viewmodel.MapPinsViewModel
import com.schwarzit.lovenpark.user.socialLogin.SocialUserSharedPrefs
import com.schwarzit.lovenpark.utils.Util

class LocationInfoWindowFragment : BottomSheetDialogFragment() {

    private var binding: FragmentLocationInfoWindowBinding? = null
    private var adapter: LocationPicturesAdapter = LocationPicturesAdapter()
    private val mapPinsViewModel: MapPinsViewModel by activityViewModels()
    private var selectedMarker: Marker? = null
    private var listener: OnOptionClickListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setLocationsPictures(emptyList())
        binding?.apply {
            locationPictures?.adapter = adapter
            createSignalButton.setOnClickListener {
                if (isUserLogged() == true) {
                    listener?.onOptionClick(Option.CREATE_SIGNAL).also { dismiss() }
                } else {
                    checkNavigationFeatureFlagStatus()
                }
            }
            locationSave.setOnClickListener {
                if (isUserLogged() == true) {
                    listener?.onOptionClick(Option.SAVE_TO_FAVOURITE)
                    mapPinsViewModel.selectedPin.value?.let { selectedMapPin ->
                        mapPinsViewModel.updateFavouriteMapPinInDatabase(selectedMapPin.id)
                        selectedMapPin.isBookmarked = !selectedMapPin.isBookmarked
                        if (selectedMapPin.isBookmarked) {
                            binding?.locationSave?.setImageResource(R.drawable.ic_save_button_dark)
                        } else {
                            binding?.locationSave?.setImageResource(R.drawable.ic_save_button)
                        }
                    }
                } else {
                    checkNavigationFeatureFlagStatus()
                }

            }
        }
    }


    private fun checkNavigationFeatureFlagStatus() =
        if (FeatureFlags.FLAG_LOGIN_REGISTRATION.isActive) {
            listener?.onOptionClick(Option.GENERIC_LOGIN_REGISTER).also { dismiss() }
        } else {
            listener?.onOptionClick(Option.SOCIAL_LOGIN_REGISTER).also { dismiss() }
        }

    fun setOnOptionClickListener(listener: OnOptionClickListener) {
        this.listener = listener
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    fun setLastSelectedMarker(marker: Marker?) {
        selectedMarker = marker
    }

    override fun onCancel(dialog: DialogInterface) {
        val iconGenerator = IconGenerator(context)
        val markerImageView = ImageView(context)
        iconGenerator.setContentView(markerImageView)

        val unselectedPinIcon = setUnselectedPinIcon(mapPinsViewModel.selectedPin.value)
        markerImageView.setImageResource(unselectedPinIcon)
        iconGenerator.setBackground(null)
        selectedMarker?.setIcon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()))
        super.onCancel(dialog)
    }

    private fun setUnselectedPinIcon(lastSelectedPin: MapPinUIModel?) =
        when (lastSelectedPin?.category) {
            PinCategory.BENCH -> R.drawable.ic_pin_bench_unselected
            PinCategory.BICYCLE -> R.drawable.ic_pin_bike_unselected
            PinCategory.TRASH -> R.drawable.ic_pin_trashcan_unselected
            PinCategory.OTHER -> R.drawable.ic_pin_tree_unselected
            else -> R.drawable.ic_pin_empty
        }

    private fun isUserLogged() =
        if (FeatureFlags.FLAG_LOGIN_REGISTRATION.isActive) {
            UserSharedPrefHelper.getLoggedUserEmail(requireContext())?.isNotEmpty()
        } else {
            SocialUserSharedPrefs.getLoggedUser(requireContext())?.isNotEmpty()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationInfoWindowBinding.inflate(inflater, container, false)

        mapPinsViewModel.selectedPin.observe(viewLifecycleOwner) { mapPin ->
            setSelectedMapPinDetails(mapPin)
        }

        dialog?.window?.setDimAmount(0.4f)

        return binding?.root
    }

    private fun setSelectedMapPinDetails(selectedMapPin: MapPinUIModel) {
        (activity as MainActivity).findViewById<TextView>(R.id.fragment_title)?.text =
            selectedMapPin.pinTitle

        binding?.apply {
            locationTitle.text = selectedMapPin.pinTitle
            locationCategory.text = selectedMapPin.description
            if (selectedMapPin.picture?.isEmpty() == true) {
                adapter.setLocationsPictures(listOf(PictureModel(Util.EMPTY_STRING)))
            } else {
                selectedMapPin.picture?.let { adapter.setLocationsPictures(it) }
            }
            locationPictures.adapter = adapter

            mapPinsViewModel.selectedPin.value?.let { selectedMapPin ->
                val getBookmarkedMapPinsInDB =
                    mapPinsViewModel.getFavouriteMapPinsFromDatabase()
                if (getBookmarkedMapPinsInDB.contains(selectedMapPin.id)) {
                    selectedMapPin.isBookmarked = true
                    binding?.locationSave?.setImageResource(R.drawable.ic_save_button_dark)
                } else {
                    selectedMapPin.isBookmarked = false
                    binding?.locationSave?.setImageResource(R.drawable.ic_save_button)
                }
            }
        }
    }

    private fun getLocationCategory(location : MapPinUIModel) =
        when (location.category) {
            PinCategory.BENCH -> R.string.map_pin_title_bench
            PinCategory.BICYCLE -> R.string.map_pin_title_bicycle
            PinCategory.TRASH -> R.string.map_pin_title_trash
            else -> R.string.map_pin_title_other
        }


    companion object {
        const val TAG = "LocationInfoWindowFragment"
    }
}

enum class Option {
    CREATE_SIGNAL,
    SAVE_TO_FAVOURITE,
    SOCIAL_LOGIN_REGISTER,
    GENERIC_LOGIN_REGISTER,
}

fun interface OnOptionClickListener {
    fun onOptionClick(option: Option)
}

