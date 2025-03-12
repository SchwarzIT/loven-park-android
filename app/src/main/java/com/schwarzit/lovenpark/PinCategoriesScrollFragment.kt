package com.schwarzit.lovenpark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.schwarzit.lovenpark.databinding.FragmentPinCategoriesScrollBinding
import com.schwarzit.lovenpark.mappin.domain.PinCategory
import com.schwarzit.lovenpark.mappin.ui.view.GoogleMapFragmentDirections
import com.schwarzit.lovenpark.mappin.ui.viewmodel.MapPinsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PinCategoriesScrollFragment : Fragment() {

    private var binding: FragmentPinCategoriesScrollBinding? = null

    private val mapPinsViewModel: MapPinsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinCategoriesScrollBinding.inflate(inflater, container, false)

        binding?.apply {
            bicycle.setOnClickListener {
                mapPinsViewModel.updateSelectedCategories(PinCategory.BICYCLE)
            }
            bench.setOnClickListener {
                mapPinsViewModel.updateSelectedCategories(PinCategory.BENCH)
            }
            trash.setOnClickListener {
                mapPinsViewModel.updateSelectedCategories(PinCategory.TRASH)
            }
            other.setOnClickListener {
                mapPinsViewModel.updateSelectedCategories(PinCategory.OTHER)
            }
            menu.setOnClickListener {
                menu.isChecked = false
                navigateToLocations()
            }
        }
        return binding?.root
    }

    private fun navigateToLocations() {
        findNavController().navigate(GoogleMapFragmentDirections.actionGoogleMapFragmentToLocationsFragmet())
    }

    override fun onResume() {
        super.onResume()
        setCategoriesCheckedState()
    }

    private fun setCategoriesCheckedState() {
        binding?.apply {
            bicycle.isChecked =
                (mapPinsViewModel.selectedPinCategories.value?.contains(PinCategory.BICYCLE) == true)
            bench.isChecked =
                (mapPinsViewModel.selectedPinCategories.value?.contains(PinCategory.BENCH) == true)
            trash.isChecked =
                (mapPinsViewModel.selectedPinCategories.value?.contains(PinCategory.TRASH) == true)
            other.isChecked =
                (mapPinsViewModel.selectedPinCategories.value?.contains(PinCategory.OTHER) == true)

        }
    }
}