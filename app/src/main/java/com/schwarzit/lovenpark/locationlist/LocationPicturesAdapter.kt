package com.schwarzit.lovenpark.locationlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schwarzit.lovenpark.databinding.ItemLocationDetailPictureBinding
import com.schwarzit.lovenpark.mappin.data.remote.PictureModel
import com.schwarzit.lovenpark.mappin.data.remote.setGlideRes

class LocationPicturesAdapter :
    RecyclerView.Adapter<LocationPicturesAdapter.LocationPicturesRecyclerViewHolder>() {

    private var locationsPictures = emptyList<PictureModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationPicturesRecyclerViewHolder {

        val binding = ItemLocationDetailPictureBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return LocationPicturesRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationPicturesRecyclerViewHolder, position: Int) {
        holder.binding.locationPicture.setGlideRes(
            locationsPictures[position].url
        )
    }

    override fun getItemCount() = locationsPictures.size

    fun setLocationsPictures(locationsPictures: List<PictureModel>) {
        this.locationsPictures = locationsPictures
    }

    inner class LocationPicturesRecyclerViewHolder(
        val binding: ItemLocationDetailPictureBinding
    ) : RecyclerView.ViewHolder(binding.root)
}