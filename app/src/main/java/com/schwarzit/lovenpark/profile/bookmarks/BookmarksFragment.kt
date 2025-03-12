package com.schwarzit.lovenpark.profile.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.schwarzit.lovenpark.MainActivity
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.FragmentBookmarksBinding
import com.schwarzit.lovenpark.locationlist.LocationsListAdapter
import com.schwarzit.lovenpark.mappin.domain.MapPinUIModel
import com.schwarzit.lovenpark.mappin.ui.viewmodel.MapPinsViewModel

class BookmarksFragment : Fragment() {

    private var binding: FragmentBookmarksBinding? = null
    private var adapter: LocationsListAdapter = LocationsListAdapter()
    private val mapPinsViewModel: MapPinsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarksBinding.inflate(inflater, container, false)

        mapPinsViewModel.saveMapPinInDatabase(sortLocationsBySelectedCategories())

        mapPinsViewModel.selectedPinCategories.observe(viewLifecycleOwner) {
            adapter.setLocationsList(sortLocationsBySelectedCategories())
            adapter.onCreateSignalClick = {
                navigateToSignalRootFragment()
            }
            binding?.locationsList?.adapter = adapter
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val getBookmarkedMapPinsInDB = mapPinsViewModel.getFavouriteMapPinsFromDatabase()
        mapPinsViewModel.liveDataMapPins.value?.let {
            adapter.setLocationsList(it.filter {mapPin ->
                getBookmarkedMapPinsInDB.contains(mapPin.id)
            })
        }
        binding?.locationsList?.adapter = adapter

        (activity as MainActivity).apply {
            findViewById<TextView>(R.id.fragment_title)?.text = getString(R.string.bookmarks)
            findViewById<TextView>(R.id.fragment_help_text)?.text =
                getString(R.string.bookmarks_description)
            findViewById<TextView>(R.id.fragment_help_text)?.isVisible = true
        }
    }

    private fun sortLocationsBySelectedCategories(): List<MapPinUIModel> {
        val getBookmarkedMapPinsInDB = mapPinsViewModel.getFavouriteMapPinsFromDatabase()
        return mapPinsViewModel.filterPinsFromSelectedCategories()?.filter {
            getBookmarkedMapPinsInDB.contains(it.id)
        }.orEmpty()
    }

    private fun navigateToSignalRootFragment() {
        findNavController().navigate(BookmarksFragmentDirections.actionBookmarksFragmentToSignalRootFragment())
    }
}