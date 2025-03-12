package com.schwarzit.lovenpark.locationlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.ItemLocationCardBinding
import com.schwarzit.lovenpark.mappin.data.remote.setGlideRes
import com.schwarzit.lovenpark.mappin.domain.MapPinUIModel
import com.schwarzit.lovenpark.mappin.domain.PinCategory

class LocationsListAdapter :
    RecyclerView.Adapter<LocationsListAdapter.LocationsListRecyclerViewHolder>() {

    private var locationsList = emptyList<MapPinUIModel>()
    var onCreateSignalClick: (() -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationsListRecyclerViewHolder {

        val binding = ItemLocationCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return LocationsListRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationsListRecyclerViewHolder, position: Int) {
        holder.binding.apply {
            locationImage.setGlideRes(
                locationsList[position].picture?.firstOrNull()?.url
            )
            locationCategory.text = locationsList[position].description
            locationTitle.text = locationsList[position].pinTitle
            createSignalButton.setOnClickListener {
                onCreateSignalClick?.invoke()
            }
            if (locationsList[position].isBookmarked) {
                bookmarkLocation.setImageResource(R.drawable.ic_bookmark_dark)
            } else {
                bookmarkLocation.setImageResource(R.drawable.ic_bookmark)
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

    override fun getItemCount() = locationsList.size

    fun setLocationsList(locationsList: List<MapPinUIModel>) {
        this.locationsList = locationsList
    }

    inner class LocationsListRecyclerViewHolder(
        val binding: ItemLocationCardBinding
    ) : RecyclerView.ViewHolder(binding.root)
}